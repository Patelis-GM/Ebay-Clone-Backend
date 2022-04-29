package silkroad.dtos.user;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsDTO implements Serializable {

    private String username;
    private String password;
}
