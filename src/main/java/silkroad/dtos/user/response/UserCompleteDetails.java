package silkroad.dtos.user.response;

import lombok.Data;
import silkroad.entities.Address;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserCompleteDetails implements Serializable {
    private final String username;
    private final String email;
    private final String role;
    private final Boolean approved;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String tin;
    private final Double buyerRating;
    private final Double sellerRating;
    private final Address address;
    private final Date joinDate;
}
