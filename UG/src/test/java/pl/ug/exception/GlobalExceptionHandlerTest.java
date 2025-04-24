package pl.ug.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.ug.dto.ExceptionDto;
import pl.ug.model.log.ExceptionLog;
import pl.ug.service.LogService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private LogService logService;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleGeneralAppExceptionReturnsCorrectResponse() {
        GeneralAppException exception = new GeneralAppException("General error", HttpStatus.BAD_REQUEST);

        ResponseEntity<ExceptionDto> response = globalExceptionHandler.handleGeneralAppException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("General error", response.getBody().message());
        verify(logService).saveLog(any(ExceptionLog.class));
    }

    @Test
    void handleConstraintViolationExceptionReturnsValidationErrors() {
        ConstraintViolationException exception = new ConstraintViolationException(
                Set.of(
                        mockConstraintViolation("field1", "must not be null"),
                        mockConstraintViolation("field2", "must be a valid email")
                )
        );

        ResponseEntity<ExceptionDto> response = globalExceptionHandler.handleConstraintViolationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<String> messages = (List<String>) response.getBody().message();

        assertTrue(messages.contains("field1: must not be null"));
        assertTrue(messages.contains("field2: must be a valid email"));
        verify(logService).saveLog(any(ExceptionLog.class));
    }

    @Test
    void handleValidationExceptionsReturnsFieldErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("objectName", "field1", "must not be null"),
                new FieldError("objectName", "field2", "must be a valid email")
        ));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationErrors(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("must not be null", response.getBody().get("field1"));
        assertEquals("must be a valid email", response.getBody().get("field2"));
        verify(logService).saveLog(any(ExceptionLog.class));
    }

    @Test
    void handleOtherExceptionsReturnsInternalServerError() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ExceptionDto> response = globalExceptionHandler.handleOtherExceptions(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Object message = response.getBody().message();
        assertInstanceOf(String.class, message);
        assertTrue(((String) message).contains("Unexpected error"));

        verify(logService).saveLog(any(ExceptionLog.class));
    }

    private ConstraintViolation<?> mockConstraintViolation(String propertyPath, String message) {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);
        when(path.toString()).thenReturn(propertyPath);
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn(message);
        return violation;
    }
}
