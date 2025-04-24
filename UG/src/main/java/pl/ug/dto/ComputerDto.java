package pl.ug.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComputerDto(
        Long id,
        String name,
        LocalDateTime postingDate,
        LocalDateTime currencyConversionDate,
        BigDecimal priceInPln,
        BigDecimal priceInUsd
) {
}