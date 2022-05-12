package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silkroad.dtos.message.MessageMapper;
import silkroad.dtos.message.request.MessagePosting;
import silkroad.dtos.message.response.ReceivedMessageDetails;
import silkroad.dtos.message.response.SentMessageDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.entities.Message;
import silkroad.entities.Message_;
import silkroad.entities.User;
import silkroad.exceptions.MessageException;
import silkroad.exceptions.UserException;
import silkroad.repositories.MessageRepository;
import silkroad.repositories.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Transactional
    public void sendMessage(Authentication authentication, MessagePosting messagePosting) {

        String recipientUsername = messagePosting.getRecipient();

        if (!this.userRepository.existsById(recipientUsername))
            throw new UserException(UserException.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

        User sender = this.userRepository.getById(authentication.getName());
        User recipient = this.userRepository.getById(recipientUsername);

        Message message = new Message(messagePosting.getTitle(), messagePosting.getBody(), sender, recipient);
        message = this.messageRepository.save(message);

        sender.grantAccessToMessage(message);
        recipient.grantAccessToMessage(message);

        this.userRepository.save(sender);
        this.userRepository.save(recipient);
    }

    @Transactional
    public void readUserMessage(Authentication authentication, Long messageID) {

        if (!this.messageRepository.existsById(messageID))
            throw new MessageException(MessageException.NOT_FOUND, HttpStatus.NOT_FOUND);

        if (!this.messageRepository.findRecipientById(messageID).equals(authentication.getName()))
            throw new UserException(UserException.USER_ACTION_FORBIDDEN, HttpStatus.FORBIDDEN);

        this.messageRepository.readMessage(messageID, authentication.getName(), true);
    }

    @Transactional
    public void deleteUserMessage(Authentication authentication, Long messageID) {

        if (!this.messageRepository.existsById(messageID))
            throw new MessageException(MessageException.NOT_FOUND, HttpStatus.NOT_FOUND);

        User user = this.userRepository.getById(authentication.getName());
        Message message = this.messageRepository.getById(messageID);
        user.removeAccessToMessage(message);
        this.userRepository.save(user);
    }

    public PageResponse<ReceivedMessageDetails> getUserReceivedMessages(Authentication authentication, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Message_.CREATION_DATE).descending());
        Page<Message> messagePage = this.messageRepository.getReceivedMessages(authentication.getName(), pageRequest);
        List<ReceivedMessageDetails> receivedMessageDetailsList = this.messageMapper.toReceivedMessageDetailsList(messagePage.getContent());
        return new PageResponse<>(receivedMessageDetailsList, page + 1, messagePage.getTotalPages(), messagePage.getTotalElements(), messagePage.getNumberOfElements());
    }

    public PageResponse<SentMessageDetails> getUserSentMessages(Authentication authentication, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Message_.CREATION_DATE).descending());
        Page<Message> messagePage = this.messageRepository.getSentMessages(authentication.getName(), pageRequest);
        List<SentMessageDetails> sentMessageDetailsList = this.messageMapper.toSentMessageDetailsList(messagePage.getContent());
        return new PageResponse<>(sentMessageDetailsList, page + 1, messagePage.getTotalPages(), messagePage.getTotalElements(), messagePage.getNumberOfElements());
    }


}
