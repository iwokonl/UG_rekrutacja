package pl.ug.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.config.XmlDeserializer.XmlDeserializer;
import pl.ug.dto.ComputerDto;
import pl.ug.dto.ComputerDtoXml;
import pl.ug.dto.CreateComputerDto;
import pl.ug.dto.NbpResponseDto;
import pl.ug.exception.GeneralAppException;
import pl.ug.mapper.ComputerMapper;
import pl.ug.model.Computer;
import pl.ug.repository.ComputerRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComputerService {


    private final ComputerRepository computerRepository;
    private final XmlDeserializer xmlDeserializer;
    private final NbpApiService nbpApiService;
    private final Path xmlFilePath;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Transactional
    public ComputerDto saveComputer(CreateComputerDto createComputerDto) {
        Computer computer = ComputerMapper.mapCreateComputerDtoToComputer(createComputerDto);
        computer.prePersist();

        NbpResponseDto nbpResponseDto = nbpApiService.callApiNbpForExchangeRate("USD", computer.getCurrencyConversionDate());
        BigDecimal dollarExchangeRate = new BigDecimal(String.valueOf(nbpResponseDto.rates().getFirst().ask()));
        computer.setPriceInUsd(computer.getPriceInUsd().setScale(2, RoundingMode.HALF_EVEN));
        computer.setPriceInPln(
                dollarExchangeRate
                        .multiply(computer.getPriceInUsd())
                        .setScale(2, RoundingMode.HALF_UP)
        );
        computer.setCurrencyConversionDate(LocalDate.parse(nbpResponseDto.rates().getFirst().effectiveDate()).atStartOfDay());
        computerRepository.save(computer);
        log.info("Saving computer {}", computer);

        ComputerDto computerDto = ComputerMapper.mapComputerToComputerDto(computer);

        try {

            xmlDeserializer.addComputerToFile(computer);
        } catch (IOException e) {
            throw new GeneralAppException("Problem with writing to file",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return computerDto;
    }


    @Transactional(readOnly = true)
    public List<ComputerDto> searchByNameFragmentAndDateRangePosting(String name, LocalDate dateFrom, LocalDate dateTo) {
        String searchName = Optional.ofNullable(name).orElse("");

        List<Computer> computers;

        if (dateFrom != null || dateTo != null) {
            LocalDateTime from = Optional.ofNullable(dateFrom)
                    .map(LocalDate::atStartOfDay)
                    .orElse(LocalDate.of(1, 1, 1).atStartOfDay());

            LocalDateTime to = Optional.ofNullable(dateTo)
                    .map(LocalDate::atStartOfDay)
                    .orElse(LocalDate.of(2200, 1, 1).atStartOfDay());

            computers = computerRepository.searchByNameFragmentAndDateRangePosting(searchName, from, to);
        } else {
            computers = computerRepository.searchByNameFragment(searchName);
        }

        if (computers.isEmpty()) {
            log.warn("No computer with the specified name found");
            throw new GeneralAppException("No computer with the specified name found", HttpStatus.NOT_FOUND);
        }

        return computers.stream()
                .map(ComputerMapper::mapComputerToComputerDto)
                .toList();
    }

}

