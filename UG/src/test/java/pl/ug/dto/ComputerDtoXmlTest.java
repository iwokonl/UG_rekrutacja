package pl.ug.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComputerDtoXmlTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validComputerDtoXmlShouldPassValidation() {
        ComputerDtoXml dto = new ComputerDtoXml(
                1L,
                "Office PC",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(2000.00),
                BigDecimal.valueOf(500.00)
        );

        Set<ConstraintViolation<ComputerDtoXml>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullFieldsInComputerDtoXmlShouldFailValidation() {
        ComputerDtoXml dto = new ComputerDtoXml(
                null,
                null,
                null,
                null,
                null,
                null
        );

        Set<ConstraintViolation<ComputerDtoXml>> violations = validator.validate(dto);
        assertEquals(4, violations.size());
    }

    @Test
    void negativePriceInPlnShouldFailValidation() {
        ComputerDtoXml dto = new ComputerDtoXml(
                1L,
                "Office PC",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(-100.00),
                BigDecimal.valueOf(500.00)
        );

        Set<ConstraintViolation<ComputerDtoXml>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void negativePriceInUsdShouldFailValidation() {
        ComputerDtoXml dto = new ComputerDtoXml(
                1L,
                "Office PC",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(2000.00),
                BigDecimal.valueOf(-50.00)
        );

        Set<ConstraintViolation<ComputerDtoXml>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void blankNameShouldFailValidation() {
        ComputerDtoXml dto = new ComputerDtoXml(
                1L,
                " ",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(2000.00),
                BigDecimal.valueOf(500.00)
        );

        Set<ConstraintViolation<ComputerDtoXml>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}