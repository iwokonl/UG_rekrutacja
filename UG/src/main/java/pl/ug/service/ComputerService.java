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

        synchronized (this) {
            try {
                File outputFile = xmlFilePath.toFile();

                if (!outputFile.exists()) {
                    if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
                        outputFile.getParentFile().mkdirs();
                    }
                    outputFile.createNewFile(); //
                    log.info("Created new XML file: {}", outputFile.getAbsolutePath());
                }

                List<ComputerDtoXml> existingComputers = outputFile.length() > 0
                        ? xmlDeserializer.readComputersFromFile(outputFile)
                        : List.of();

                List<ComputerDtoXml> updatedComputers = new ArrayList<>(existingComputers);
                updatedComputers.add(ComputerMapper.mapComputerToComputerDtoXml(computer));

                updatedComputers.stream()
                        .forEach(e -> {
                            Set<ConstraintViolation<ComputerDtoXml>> violations = validator.validate(e);
                            if (!violations.isEmpty()) {
                                throw new GeneralAppException(
                                        "Validation failed for computer: " + e.name() + ". Violations: " + violations,
                                        HttpStatus.BAD_REQUEST
                                );
                            }
                        });

                xmlDeserializer.writeComputersToFile(updatedComputers, outputFile);
                log.info("Added computer to XML file: {}", outputFile.getAbsolutePath());
            } catch (IOException e) {
                log.error("Failed to write computer to XML file: {}", e.getMessage(), e);
                throw new GeneralAppException("Failed to write computer to XML file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
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

