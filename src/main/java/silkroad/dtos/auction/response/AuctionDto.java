package silkroad.dtos.auction.response;

import lombok.Data;
import silkroad.entities.Address;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class AuctionDto implements Serializable {
    private final Long id;
    private final String name;
    private final String description;
    private final Date startDate;
    private final Date endDate;
    private final Double buyPrice;
    private final Double firstBid;
    private final Long totalBids;
    private final Double highestBid;
    private final String bidder;
    private final Set<String> categories;
    private final List<String> images;
    private final Address address;
}
