package silkroad.dtos.user.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserBasicDetails implements Serializable {
    
    private final String username;
    private final String role;
    private final Boolean approved;
    private final Date joinDate;
}
