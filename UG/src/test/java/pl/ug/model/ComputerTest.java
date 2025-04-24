package pl.ug.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ComputerTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void prePersistSetsPostingDateAndCurrencyConversionDateWhenNull() {
        Computer computer = Computer.builder()
                .name("Test Computer")
                .priceInPln(BigDecimal.valueOf(1000))
                .priceInUsd(BigDecimal.valueOf(250))
                .build();

        computer.prePersist();

        assertNotNull(computer.getPostingDate());
        assertNotNull(computer.getCurrencyConversionDate());
    }

    @Test
    void prePersistDoesNotOverrideExistingDates() {
        LocalDateTime existingDate = LocalDateTime.of(2023, 1, 1, 12, 0);
        Computer computer = Computer.builder()
                .name("Test Computer")
                .postingDate(existingDate)
                .currencyConversionDate(existingDate)
                .priceInPln(BigDecimal.valueOf(1000))
                .priceInUsd(BigDecimal.valueOf(250))
                .build();

        computer.prePersist();

        assertEquals(existingDate, computer.getPostingDate());
        assertEquals(existingDate, computer.getCurrencyConversionDate());
    }

    @Test
    void validationFailsForNegativePriceInPLN() {
        Computer computer = Computer.builder()
                .name("Test Computer")
                .priceInPln(BigDecimal.valueOf(-100))
                .priceInUsd(BigDecimal.valueOf(250))
                .build();

        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Price must be zero or positive")));
    }

    @Test
    void validationFailsForExcessiveFractionDigitsInPriceInPLN() {
        Computer computer = Computer.builder()
                .name("Test Computer")
                .priceInPln(new BigDecimal("1000.123"))
                .priceInUsd(BigDecimal.valueOf(250))
                .build();

        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Price must have at most 2 decimal places")));
    }

    @Test
    void validationFailsForNullName() {
        Computer computer = Computer.builder()
                .name(null)
                .priceInPln(BigDecimal.valueOf(1000))
                .priceInUsd(BigDecimal.valueOf(250))
                .build();

        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Name cannot be blank in Computer")));
    }

    @Test
    void validationPassesForValidComputer() {
        Computer computer = Computer.builder()
                .name("Valid Computer")
                .priceInPln(BigDecimal.valueOf(1000))
                .priceInUsd(BigDecimal.valueOf(250))
                .build();

        computer.prePersist();

        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);

        assertTrue(violations.isEmpty());
        assertNotNull(computer.getPostingDate()); // Sprawdzenie, czy data została ustawiona
        assertNotNull(computer.getCurrencyConversionDate()); // Sprawdzenie, czy data została ustawiona
    }
}