package silkroad.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

public class UserException extends SilkRoadException {

    public static final String USER_NOT_FOUND = "UE_001";
    public static final String USER_ACTION_FORBIDDEN = "UE_002";
    public static final String USER_USERNAME_EXISTS = "UE_003";
    public static final String USER_EMAIL_EXISTS = "UE_004";
    public static final String USER_BAD_CREDENTIALS = "UE_005";
    public static final String USER_DISABLED = "UE_006";

    public UserException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public UserException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }

    public static void validateAuthentication(Authentication authentication, String userResource) {
        if (!authentication.getName().equals(userResource))
            throw new UserException(USER_ACTION_FORBIDDEN, HttpStatus.FORBIDDEN);
    }

}
