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
    ReceivedMessageDetails mapToReceivedMessageDetails(Message message);

    List<ReceivedMessageDetails> mapToReceivedMessageDetailsList(List<Message> messages);

    @Mapping(target = "sender", source = "sender.username")
    @Mapping(target = "recipient", source = "recipient.username")
    SentMessageDetails mapToSentMessageDetails(Message message);

    List<SentMessageDetails> mapToSentMessageDetailsList(List<Message> messages);


}
