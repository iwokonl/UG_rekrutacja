package pl.ug.model.log;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class InfoLog extends AbstractLog {

    public InfoLog() {
        super.typeOfLog = "InfoLog";
    }

    public InfoLog(String message) {
        super.message = message;
        super.typeOfLog = "InfoLog";
    }

    public InfoLog(String message, String typeOfLog) {
        super.message = message;
        super.typeOfLog = typeOfLog;
    }


}
