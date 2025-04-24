package pl.ug.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonFilter("ComputerDtoFilter")
public record ComputerDtoXml(
        Long id,

        @NotBlank(message = "Name cannot be null")
        @JacksonXmlProperty(localName = "nazwa")
        String name,

        @NotNull(message = "Posting date cannot be null")
        @JacksonXmlProperty(localName = "data_ksiegowania")
        LocalDateTime postingDate,

        LocalDateTime currencyConversionDate,

        @JacksonXmlProperty(localName = "koszt_PLN")
        @NotNull(message = "Price in PLN cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price in PLN must be greater than 0")
        BigDecimal priceInPln,

        @JacksonXmlProperty(localName = "koszt_USD")
        @NotNull(message = "Price in USD cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price in USD must be greater than 0")
        BigDecimal priceInUsd
) {
}