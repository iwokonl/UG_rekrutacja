package pl.ug.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.ug.dto.ExceptionDto;
import pl.ug.model.log.ExceptionLog;
import pl.ug.service.LogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final LogService logService;

    public GlobalExceptionHandler(LogService logService) {
        this.logService = logService;
    }

    @ExceptionHandler(GeneralAppException.class)
    public ResponseEntity<ExceptionDto> handleGeneralAppException(GeneralAppException ex) {
        log.error("GeneralAppException: {}", ex.getMessage(), ex);
        logService.saveLog(new ExceptionLog(ex.getMessage(), ex.getStatus()));
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage()), ex.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDto> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        log.error("ConstraintViolationException: {}", errors, ex);
        logService.saveLog(new ExceptionLog("Validation error: " + errors, HttpStatus.BAD_REQUEST));

        return new ResponseEntity<>(new ExceptionDto(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.transaction.TransactionSystemException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(org.springframework.transaction.TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();
        log.error("TransactionSystemException: {}", ex.getMessage(), ex);
        logService.saveLog(new ExceptionLog(ex.getMessage(), HttpStatus.BAD_REQUEST));

        if (cause instanceof ConstraintViolationException violationException) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<?> violation : violationException.getConstraintViolations()) {
                String fieldName = violation.getPropertyPath().toString();
                String errorMessage = violation.getMessage();
                errors.put(fieldName, errorMessage);
            }

            log.error("ConstraintViolations in transaction: {}", errors);
            logService.saveLog(new ExceptionLog("Validation error: " + errors, HttpStatus.BAD_REQUEST));

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(Map.of("error", "Unexpected transaction error"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        log.error("MethodArgumentNotValidException: {}", errors, ex);
        logService.saveLog(new ExceptionLog("Validation error: " + errors, HttpStatus.BAD_REQUEST));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleOtherExceptions(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        logService.saveLog(new ExceptionLog("Unhandled: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<>(new ExceptionDto(ex.getClass() + ": " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
