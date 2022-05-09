package silkroad.dtos.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserBasicDetails implements Serializable {
    private final String username;
    private final String role;
    private final Boolean approved;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date joinDate;
}
