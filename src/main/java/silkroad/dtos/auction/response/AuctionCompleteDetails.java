package silkroad.dtos.auction.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import silkroad.entities.Address;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class AuctionCompleteDetails implements Serializable {

    private final Long id;
    private final String name;
    private final String description;
    private final Date startDate;
    private final Date endDate;
    private final Double buyPrice;
    private final Double firstBid;
    private final Long totalBids;
    private final Double highestBid;
    private final String seller;
    private final Double sellerRating;
    private final List<String> images;
    private final List<String> categories;
    private final Address address;
    private final Boolean expired;
    private final Long version;
}
