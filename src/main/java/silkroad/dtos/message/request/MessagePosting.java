package silkroad.dtos.message.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessagePosting implements Serializable {

    private final String title;

    private final String body;

    private final String recipient;
}
