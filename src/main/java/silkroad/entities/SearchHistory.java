package silkroad.entities;

import lombok.*;


import javax.persistence.*;

@Entity
@Table(name = "searchhistory")
@NoArgsConstructor
@Getter
@Setter
public class SearchHistory {

    @EmbeddedId
    private SearchHistoryID id;

    @MapsId("auctionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "interactions", nullable = false)
    private Long interactions;

    public SearchHistory(SearchHistoryID id, Auction auction, User user) {
        this.id = id;
        this.auction = auction;
        this.user = user;
        this.interactions = 1L;
    }
}