package silkroad.dtos.user.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class Username implements Serializable {
    private final String username;
}
