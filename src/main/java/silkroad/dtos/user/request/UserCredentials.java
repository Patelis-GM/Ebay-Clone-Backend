package silkroad.dtos.user.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials implements Serializable {

    private String username;
    private String password;
}
