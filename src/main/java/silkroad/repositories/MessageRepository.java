package silkroad.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.read = ?3 WHERE m.id = ?1 AND m.recipient.username = ?2 ")
    Integer readMessage(Long messageID, String username, Boolean readStatus);

    @Query("SELECT m FROM Message m JOIN m.recipient recipient WHERE recipient.username = ?1 and m MEMBER OF recipient.accessedMessages")
    Page<Message> getReceivedMessages(String username, Pageable pageable);

    @Query("SELECT m FROM Message m JOIN m.sender sender WHERE sender.username = ?1 and m MEMBER OF sender.accessedMessages")
    Page<Message> getSentMessages(String username, Pageable pageable);

    @Query("SELECT m.recipient.username FROM Message m WHERE m.id =?1")
    String findRecipientById(Long messageID);
}