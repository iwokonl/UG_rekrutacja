package pl.ug.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.ug.validation.notfuture.NotFutureCanBeNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateComputerDto(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotFutureCanBeNull(message = "Posting date cannot be in the future")
        LocalDateTime postingDate,

        @NotFutureCanBeNull(message = "Currency conversion date cannot be in the future")
        LocalDateTime currencyConversionDate,

        @DecimalMin(value = "0.0", inclusive = true, message = "Price in USD must be greater than or equal to 0")
        @Digits(integer = 10, fraction = 2, message = "Price in USD must have at most 10 digits before the decimal point and 2 digits after the decimal point")
        @NotNull(message = "Price in USD cannot be null")
        BigDecimal priceInUsd) {
}
