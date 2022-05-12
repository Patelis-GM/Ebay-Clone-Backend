package silkroad.dtos.user.request;

import lombok.Data;
import silkroad.entities.Address;

import java.io.Serializable;

@Data
public class UserRegistration implements Serializable {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String tin;

    private Address address;
}
