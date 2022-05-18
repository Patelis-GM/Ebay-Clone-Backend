package silkroad.dtos.auction.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AuctionBasicDetails implements Serializable {

    private final Long id;
    private final String name;
    private final Date endDate;
    private final Double buyPrice;
    private final Double firstBid;
    private final Double highestBid;
    private final Boolean expired;
    private final List<String> images;
    private final String bidder;
}
