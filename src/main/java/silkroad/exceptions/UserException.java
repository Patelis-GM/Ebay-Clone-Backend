package silkroad.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends SilkRoadException{

    public static final String NOT_FOUND = "UE_001";

    public UserException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public UserException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }
}
