package silkroad.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import silkroad.utilities.TimeManager;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Lob
    @Column(name = "title", nullable = false, updatable = false)
    private String title;

    @Lob
    @Column(name = "body", nullable = false, updatable = false)
    private String body;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate;

    @Column(name = "read", nullable = false)
    private Boolean read;

    @Column(name = "deleted_by_sender", nullable = false)
    private Boolean deletedBySender;

    @Column(name = "deleted_by_recipient", nullable = false)
    private Boolean deletedByRecipient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient", nullable = false)
    private User recipient;


    public Message(String title, String body, User sender, User recipient) {
        this.title = title;
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
        this.read = false;
        this.creationDate = TimeManager.now();
        this.deletedBySender = false;
        this.deletedByRecipient = false;
    }

}