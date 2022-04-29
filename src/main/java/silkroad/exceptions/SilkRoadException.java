package silkroad.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SilkRoadException extends RuntimeException {

    private final String code;
    private final HttpStatus httpStatus;

    public SilkRoadException(String message, String code, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public SilkRoadException(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
