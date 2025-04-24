package pl.ug.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GeneralAppExceptionTest {
    @Test
    void generalAppExceptionReturnsCorrectStatusAndMessage() {
        GeneralAppException exception = new GeneralAppException("Error occurred", HttpStatus.NOT_FOUND);

        assertEquals("Error occurred", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void generalAppExceptionHandlesNullMessage() {
        GeneralAppException exception = new GeneralAppException(null, HttpStatus.INTERNAL_SERVER_ERROR);

        assertNull(exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

}