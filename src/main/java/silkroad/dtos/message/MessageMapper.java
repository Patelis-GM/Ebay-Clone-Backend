package silkroad.dtos.message;

import org.mapstruct.*;
import silkroad.dtos.message.response.ReceivedMessageDetails;
import silkroad.dtos.message.response.SentMessageDetails;
import silkroad.entities.Message;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "sender", source = "sender.username")
    @Mapping(target = "recipient", source = "recipient.username")
    ReceivedMessageDetails toReceivedMessageDetails(Message message);

    List<ReceivedMessageDetails> toReceivedMessageDetailsList(List<Message> messages);

    @Mapping(target = "sender", source = "sender.username")
    @Mapping(target = "recipient", source = "recipient.username")
    SentMessageDetails toSentMessageDetails(Message message);

    List<SentMessageDetails> toSentMessageDetailsList(List<Message> messages);

}
