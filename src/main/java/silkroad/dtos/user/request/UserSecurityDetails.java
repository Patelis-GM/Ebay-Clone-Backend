package silkroad.dtos.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class UserSecurityDetails implements Serializable {

    private final String username;

    private final String password;

    private final Boolean approved;

    private final String role;
}
