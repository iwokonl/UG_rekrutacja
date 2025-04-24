package pl.ug.mapper;

import org.junit.jupiter.api.Test;
import pl.ug.dto.ComputerDto;
import pl.ug.dto.ComputerDtoXml;
import pl.ug.dto.CreateComputerDto;
import pl.ug.model.Computer;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ComputerMapperTest {

    @Test
    void mapComputerToComputerDtoXmlShouldMapAllFieldsCorrectly() {
        Computer computer = new Computer(1L, "TestComputer", LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay(), new BigDecimal("450.00"), new BigDecimal("100.00"));

        ComputerDtoXml result = ComputerMapper.mapComputerToComputerDtoXml(computer);

        assertEquals(computer.getId(), result.id());
        assertEquals(computer.getName(), result.name());
        assertEquals(computer.getPostingDate(), result.postingDate());
        assertEquals(computer.getCurrencyConversionDate(), result.currencyConversionDate());
        assertEquals(computer.getPriceInPln(), result.priceInPln());
        assertEquals(computer.getPriceInUsd(), result.priceInUsd());
    }

    @Test
    void mapComputerToComputerDtoShouldMapAllFieldsCorrectly() {
        Computer computer = new Computer(1L, "TestComputer", LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay(), new BigDecimal("450.00"), new BigDecimal("100.00"));

        ComputerDto result = ComputerMapper.mapComputerToComputerDto(computer);

        assertEquals(computer.getId(), result.id());
        assertEquals(computer.getName(), result.name());
        assertEquals(computer.getPostingDate(), result.postingDate());
        assertEquals(computer.getCurrencyConversionDate(), result.currencyConversionDate());
        assertEquals(computer.getPriceInPln(), result.priceInPln());
        assertEquals(computer.getPriceInUsd(), result.priceInUsd());
    }

    @Test
    void mapComputerToCreateComputerDtoShouldMapAllFieldsCorrectly() {
        Computer computer = new Computer(1L, "TestComputer", LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay(), new BigDecimal("450.00"), new BigDecimal("100.00"));

        CreateComputerDto result = ComputerMapper.mapComputerToCreateComputerDto(computer);

        assertEquals(computer.getName(), result.name());
        assertEquals(computer.getPostingDate(), result.postingDate());
        assertEquals(computer.getCurrencyConversionDate(), result.currencyConversionDate());
        assertEquals(computer.getPriceInUsd(), result.priceInUsd());
    }

    @Test
    void mapComputerDtoToComputerShouldMapAllFieldsCorrectly() {
        ComputerDto computerDto = new ComputerDto(1L, "TestComputer", LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay(), new BigDecimal("450.00"), new BigDecimal("100.00"));

        Computer result = ComputerMapper.mapComputerDtoToComputer(computerDto);

        assertEquals(computerDto.id(), result.getId());
        assertEquals(computerDto.name(), result.getName());
        assertEquals(computerDto.postingDate(), result.getPostingDate());
        assertEquals(computerDto.currencyConversionDate(), result.getCurrencyConversionDate());
        assertEquals(computerDto.priceInPln(), result.getPriceInPln());
        assertEquals(computerDto.priceInUsd(), result.getPriceInUsd());
    }

    @Test
    void mapCreateComputerDtoToComputerShouldMapAllFieldsCorrectly() {
        CreateComputerDto createComputerDto = new CreateComputerDto("TestComputer", LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay(), new BigDecimal("100.00"));

        Computer result = ComputerMapper.mapCreateComputerDtoToComputer(createComputerDto);

        assertNull(result.getId());
        assertEquals(createComputerDto.name(), result.getName());
        assertEquals(createComputerDto.postingDate(), result.getPostingDate());
        assertEquals(createComputerDto.currencyConversionDate(), result.getCurrencyConversionDate());
        assertNull(result.getPriceInPln());
        assertEquals(createComputerDto.priceInUsd(), result.getPriceInUsd());
    }
}