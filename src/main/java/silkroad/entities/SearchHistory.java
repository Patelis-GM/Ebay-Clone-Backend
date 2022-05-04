package silkroad.entities;

import lombok.*;


import javax.persistence.*;

@Entity
@Table(name = "searchhistory")
@AllArgsConstructor
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


}