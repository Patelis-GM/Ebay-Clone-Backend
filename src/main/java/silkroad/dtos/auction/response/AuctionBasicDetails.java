package silkroad.dtos.auction.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AuctionBasicDetails implements Serializable {
    private final Long id;
    private final String name;
    private final Double buyPrice;
    private final Long totalBids;
    private final Double highestBid;
    private final List<String> images;
    private final String country;
}
