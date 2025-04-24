package pl.ug.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.ug.dto.NbpResponseDto;
import pl.ug.exception.GeneralAppException;
import pl.ug.model.log.InfoLog;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NbpApiService {
    private final WebClient nbpApiWebClient;
    private final LogService logService;
    private final Validator validator;


    public NbpResponseDto callApiNbpForExchangeRate(String currency, LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = date.minusDays(i).toLocalDate();

            String url = UriComponentsBuilder.fromUriString("https://api.nbp.pl")
                    .path("/api/exchangerates/rates/c/{currency}/{date}/")
                    .queryParam("format", "json")
                    .buildAndExpand(currency, currentDate.format(formatter))
                    .toUriString();

            log.info("Attempting to download the exchange rate from NBP. Date: {}, URL: {}", currentDate, url);
            logService.saveLog(new InfoLog("Próba pobrania kursu z NBP. Data: " + currentDate + ", URL: " + url));

            try {
                NbpResponseDto response = nbpApiWebClient.get()
                        .uri(url)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, responses -> Mono.error(new WebClientResponseException(
                                responses.statusCode().value(), "Error from NBP API", null, null, null)))
                        .bodyToMono(NbpResponseDto.class)
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                                .filter(throwable -> !(throwable instanceof WebClientResponseException
                                        && ((WebClientResponseException) throwable).getStatusCode().is4xxClientError()))
                                .doBeforeRetry(signal -> log.warn("Retrying for date: {}, reason: {}",
                                        currentDate, signal.failure().getMessage())))
                        .block();

                Set<ConstraintViolation<NbpResponseDto>> violations = validator.validate(response);
                if (!violations.isEmpty()) {
                    throw new GeneralAppException("Incorrect response from NBP API: " + violations, HttpStatus.BAD_REQUEST);
                }

                return response;
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    log.warn("No data for date {} - sample {} from 7", currentDate, i + 1);
                    logService.saveLog(new InfoLog("No data for date " + currentDate + " - trying " + (i + 1) + " of 7"));
                    continue;
                }
                throw new GeneralAppException("Error retrieving currency rate from NBP API: " + e.getMessage(),
                        HttpStatus.resolve(e.getStatusCode().value()));
            } catch (Exception e) {
                log.error("Unexpected error while downloading the exchange rate from NBP: {}", e.getMessage());
                throw new GeneralAppException("Unexpected error while downloading the exchange rate from NBP",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        throw new GeneralAppException("No exchange rate data from NBP API in the last 7 days", HttpStatus.NOT_FOUND);
    }
}