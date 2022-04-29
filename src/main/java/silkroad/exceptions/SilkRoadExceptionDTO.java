package silkroad.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import silkroad.utilities.TimeManager;

import java.util.Date;

@Getter
public class SilkRoadExceptionDTO {

    private final String exception;
    private final String message;
    private final String code;
    private final Integer status;
    private final Date timestamp;

    public SilkRoadExceptionDTO(SilkRoadException silkRoadException) {
        this.exception = silkRoadException.getClass().getName();
        this.message = silkRoadException.getMessage();
        this.code = silkRoadException.getCode();
        this.status = silkRoadException.getHttpStatus().value();
        this.timestamp = TimeManager.now();
    }

    public SilkRoadExceptionDTO(Exception exception, HttpStatus httpStatus) {
        this.exception = exception.getClass().getName();
        this.message = exception.getMessage();
        this.code = null;
        this.status = httpStatus.value();
        this.timestamp = TimeManager.now();
    }


}
