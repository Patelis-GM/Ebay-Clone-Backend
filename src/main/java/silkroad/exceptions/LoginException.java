package silkroad.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.naming.AuthenticationException;

@Getter
public class LoginException extends UsernameNotFoundException {

    public static final String LOGIN_USER_NOT_FOUND = "LE001";
    public static final String LOGIN_USER_NOT_APPROVED = "LE002";

    private final String code;

    public LoginException(String message, String code) {
        super(message);
        this.code = code;
    }
}
