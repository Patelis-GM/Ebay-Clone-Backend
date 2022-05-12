package silkroad.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

public class UserException extends SilkRoadException {

    public static final String NOT_FOUND = "UE_001";
    public static final String ACTION_FORBIDDEN = "UE_002";
    public static final String USERNAME_EXISTS = "UE_003";
    public static final String EMAIL_EXISTS = "UE_004";
    public static final String BAD_CREDENTIALS = "UE_005";
    public static final String DISABLED = "UE_006";
    public static final String UNKNOWN = "UE_007";

    public UserException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public UserException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }

    public static void validateAuthentication(Authentication authentication, String userResource) {
        if (!authentication.getName().equals(userResource))
            throw new UserException(ACTION_FORBIDDEN, HttpStatus.FORBIDDEN);
    }

}
