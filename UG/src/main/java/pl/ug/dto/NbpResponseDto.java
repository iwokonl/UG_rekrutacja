package pl.ug.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record NbpResponseDto(
        @NotNull
        String table,

        @NotNull
        String currency,

        @NotNull
        String code,

        @NotNull
        List<NbpRateDto> rates
) {

    public record NbpRateDto(
            @NotNull
            String no,

            @NotNull
            String effectiveDate,

            @NotNull
            BigDecimal bid,

            @NotNull
            BigDecimal ask
    ) {
    }

}
