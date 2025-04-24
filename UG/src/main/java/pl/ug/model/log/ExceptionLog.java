package pl.ug.model.log;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Entity
@Setter
@Getter
public class ExceptionLog extends AbstractLog {

    @NotNull
    private Long httpStatus;

    public ExceptionLog() {
        super.typeOfLog = "ExceptionLog";

    }

    public ExceptionLog(String message, HttpStatus httpStatus) {
        super.typeOfLog = "ExceptionLog";
        this.message = message;
        this.httpStatus = (long) httpStatus.value();

    }

    public ExceptionLog(String message, String typeOfLog, HttpStatus httpStatus) {
        super.typeOfLog = typeOfLog;
        this.message = message;
        this.httpStatus = (long) httpStatus.value();

    }


}
