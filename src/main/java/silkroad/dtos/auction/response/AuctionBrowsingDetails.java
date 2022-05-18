package silkroad.dtos.auction.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class AuctionBrowsingDetails implements Serializable {

    private final Long id;
    private final String name;
    private final Double buyPrice;
    private final Double firstBid;
    private final Long totalBids;
    private final Double highestBid;
    private final List<String> images;
    private final String country;
}
