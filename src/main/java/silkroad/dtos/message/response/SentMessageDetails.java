package silkroad.dtos.message.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SentMessageDetails implements Serializable {

    private final Long id;

    private final String title;

    private final String body;

    private final Date creationDate;

    private final String sender;

    private final String recipient;
}
