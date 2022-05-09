package silkroad.dtos.message.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReceivedMessageDetails implements Serializable {
    private final Long id;
    private final String title;
    private final String body;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date creationDate;
    private final Boolean read;
    private final String sender;
    private final String recipient;
}
