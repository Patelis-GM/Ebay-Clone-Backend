package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.read = ?3 WHERE m.id = ?1 AND m.recipient.username = ?2 ")
    Integer readMessage(Long messageID, String username, Boolean readStatus);

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.deletedBySender = ?3 WHERE m.id = ?1 ")
    Integer deleteSentMessage(Long messageID, Boolean deletedStatus);

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.deletedByRecipient = ?3 WHERE m.id = ?1 ")
    Integer deleteReceivedMessage(Long messageID, Boolean deletedStatus);



}