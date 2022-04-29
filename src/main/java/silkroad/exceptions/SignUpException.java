package silkroad.exceptions;

import org.springframework.http.HttpStatus;

public class SignUpException extends SilkRoadException {

    public static final String SIGNUP_USERNAME_EXISTS = "SUE_001";
    public static final String SIGNUP_EMAIL_EXISTS = "SUE_002";

    public SignUpException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }
}

