package pl.ug.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NbpResponseDtoTest {

    private final Validator validator;

    public NbpResponseDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void validNbpResponseDtoShouldPassValidation() {
        NbpResponseDto.NbpRateDto rate = new NbpResponseDto.NbpRateDto(
                "123/A/NBP/2023",
                "2023-10-01",
                BigDecimal.valueOf(4.1234),
                BigDecimal.valueOf(4.5678)
        );

        NbpResponseDto dto = new NbpResponseDto(
                "A",
                "USD",
                "USD",
                List.of(rate)
        );

        Set<ConstraintViolation<NbpResponseDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullFieldsInNbpResponseDtoShouldFailValidation() {
        NbpResponseDto dto = new NbpResponseDto(
                null,
                null,
                null,
                null
        );

        Set<ConstraintViolation<NbpResponseDto>> violations = validator.validate(dto);
        assertEquals(4, violations.size());
    }

    @Test
    void nullFieldsInNbpRateDtoShouldFailValidation() {
        NbpResponseDto.NbpRateDto rate = new NbpResponseDto.NbpRateDto(
                null,
                null,
                null,
                null
        );

        Set<ConstraintViolation<NbpResponseDto.NbpRateDto>> violations = validator.validate(rate);
        assertEquals(4, violations.size());
    }

    @Test
    void emptyRatesListShouldPassValidation() {
        NbpResponseDto dto = new NbpResponseDto(
                "A",
                "USD",
                "USD",
                List.of()
        );

        Set<ConstraintViolation<NbpResponseDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}