package pl.ug.validation.notfuture;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Clock;
import java.time.LocalDateTime;

public class NotFutureValidator implements ConstraintValidator<NotFutureCanBeNull, LocalDateTime> {

    private Clock clock = Clock.systemDefaultZone();

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.isAfter(LocalDateTime.now(clock)); // Data nie może być w przyszłości
    }


    public void setClock(Clock clock) {
        this.clock = clock;
    }
}