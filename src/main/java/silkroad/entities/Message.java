package silkroad.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import silkroad.utilities.TimeManager;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

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
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "is_read", nullable = false)
    private Boolean read;

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
        this.creationDate = TimeManager.now();
        this.read = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Message message = (Message) o;
        return id != null && Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}