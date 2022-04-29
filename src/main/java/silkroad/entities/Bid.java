package silkroad.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bid")
@Getter
@Setter
@ToString
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User bidder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auction_id", nullable = false)
    @ToString.Exclude
    private Auction auction;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "submission_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionDate;

}