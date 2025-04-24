package pl.ug.exception;

import org.springframework.http.HttpStatus;

public class GeneralAppException extends RuntimeException {
    private final HttpStatus status;

    public GeneralAppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
