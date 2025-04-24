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

class CreateComputerDtoTest {

    private final LocalDateTime fixedNow = LocalDateTime.now();
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        String timezone = System.getProperty("app.timezone", "UTC");
        System.setProperty("user.timezone", timezone);
    }

    @Test
    void validCreateComputerDtoShouldPassValidation() {
        CreateComputerDto dto = new CreateComputerDto(
                "Gaming PC",
                fixedNow,
                fixedNow,
                BigDecimal.valueOf(999.99)
        );

        Set<ConstraintViolation<CreateComputerDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankNameShouldFailValidation() {
        CreateComputerDto dto = new CreateComputerDto(
                " ",
                fixedNow,
                fixedNow,
                BigDecimal.valueOf(999.99)
        );

        Set<ConstraintViolation<CreateComputerDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void postingDateInTheFutureShouldFailValidation() {
        CreateComputerDto dto = new CreateComputerDto(
                "Gaming PC",
                fixedNow.plusDays(1),
                fixedNow,
                BigDecimal.valueOf(999.99)
        );

        Set<ConstraintViolation<CreateComputerDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Posting date cannot be in the future", violations.iterator().next().getMessage());
    }

    @Test
    void currencyConversionDateInTheFutureShouldFailValidation() {
        CreateComputerDto dto = new CreateComputerDto(
                "Gaming PC",
                fixedNow,
                fixedNow.plusDays(1),
                BigDecimal.valueOf(999.99)
        );

        Set<ConstraintViolation<CreateComputerDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Currency conversion date cannot be in the future", violations.iterator().next().getMessage());
    }

    @Test
    void nullPostingDateShouldPassValidation() {
        CreateComputerDto dto = new CreateComputerDto(
                "Gaming PC",
                null,
                fixedNow,
                BigDecimal.valueOf(999.99)
        );

        Set<ConstraintViolation<CreateComputerDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullCurrencyConversionDateShouldPassValidation() {
        CreateComputerDto dto = new CreateComputerDto(
                "Gaming PC",
                fixedNow,
                null,
                BigDecimal.valueOf(999.99)
        );

        Set<ConstraintViolation<CreateComputerDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void negativePriceInUsdShouldFailValidation() {
        CreateComputerDto dto = new CreateComputerDto(
                "Gaming PC",
                fixedNow,
                fixedNow,
                BigDecimal.valueOf(-10.00)
        );

        Set<ConstraintViolation<CreateComputerDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void priceInUsdWithTooManyFractionDigitsShouldFailValidation() {
        CreateComputerDto dto = new CreateComputerDto(
                "Gaming PC",
                fixedNow,
                fixedNow,
                new BigDecimal("999.999")
        );

        Set<ConstraintViolation<CreateComputerDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}