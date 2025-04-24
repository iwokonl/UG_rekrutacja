package pl.ug.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name cannot be blank in Computer")
    @Column(name = "nazwa", nullable = false)
    private String name;

    //    @CurrentTimestamp(source = SourceType.VM)
    @NotNull(message = "Posting date cannot be null")
    @Column(name = "data_zaksięgowania", nullable = false)
    @PastOrPresent(message = "Posting date must be in the past or present")
    private LocalDateTime postingDate;

    //    @CurrentTimestamp(source = SourceType.VM)
    @NotNull(message = "Currency conversion date cannot be null")
    @Column(name = "data_przeliczenia_kursu", nullable = false)
    @PastOrPresent(message = "Currency conversion date must be in the past or present")
    private LocalDateTime currencyConversionDate;

    @NotNull(message = "Price in Computer cannot be null")
    @PositiveOrZero(message = "Price must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 2 decimal places")
    @Column(name = "koszt_PLN", nullable = false)
    private BigDecimal priceInPln;

    @NotNull(message = "Price in Computer cannot be null")
    @PositiveOrZero(message = "Price must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 2 decimal places")
    @Column(name = "koszt_Usd", nullable = false)
    private BigDecimal priceInUsd;

    @PrePersist
    public void prePersist() {
        if (postingDate == null) {
            postingDate = LocalDateTime.now();
        }
        if (currencyConversionDate == null) {
            currencyConversionDate = LocalDateTime.now();
        }
    }
}
