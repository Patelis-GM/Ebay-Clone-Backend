package silkroad.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "bid")
@Getter
@Setter
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User bidder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "submission_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Bid bid = (Bid) o;
        return id != null && Objects.equals(id, bid.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}