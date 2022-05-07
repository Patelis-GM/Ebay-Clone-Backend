package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silkroad.dtos.message.request.MessagePosting;
import silkroad.entities.Message;
import silkroad.entities.User;
import silkroad.repositories.MessageRepository;
import silkroad.repositories.UserRepository;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendMessage(Authentication authentication, MessagePosting messagePosting) {

        String senderUsername = messagePosting.getSender();
        String recipientUsername = messagePosting.getRecipient();

        User sender = this.userRepository.getById(senderUsername);
        User recipient = this.userRepository.getById(recipientUsername);

        Message message = new Message(messagePosting.getTitle(), messagePosting.getBody(), sender, recipient);
        this.messageRepository.save(message);

    }

    @Transactional
    public void readMessage(Authentication authentication, String username, Long messageID) {
        this.messageRepository.readMessage(messageID, username, true);
    }

    @Transactional
    public void deleteSentMessage(Authentication authentication, String username, Long messageID) {
        this.messageRepository.deleteSentMessage(messageID, true);
    }

    @Transactional
    public void deleteReceivedMessage(Authentication authentication, String username, Long messageID) {
        this.messageRepository.deleteReceivedMessage(messageID, true);
    }



}
