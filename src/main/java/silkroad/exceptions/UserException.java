package silkroad.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

public class UserException extends SilkRoadException {

    public static final String NOT_FOUND = "UE_001";
    public static final String FORBIDDEN_ACTION = "UE_002";

    public UserException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public UserException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }

    public static void validateAuthentication(Authentication authentication, String userResource) {
        if (!authentication.getName().equals(userResource))
            throw new UserException(FORBIDDEN_ACTION, HttpStatus.FORBIDDEN);
    }

}
