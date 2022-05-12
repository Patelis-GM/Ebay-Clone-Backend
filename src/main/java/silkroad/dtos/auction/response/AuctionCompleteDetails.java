package silkroad.dtos.auction.response;

import lombok.Data;
import silkroad.entities.Address;
import silkroad.utilities.TimeManager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
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
    private final String bidder;
    private final Set<String> categories;
    private final List<String> images;
    private final Address address;
    private final Boolean expired;

    public AuctionCompleteDetails(Long id, String name, String description, Date startDate, Date endDate, Double buyPrice, Double firstBid, Long totalBids, Double highestBid, String bidder, Set<String> categories, List<String> images, Address address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.buyPrice = buyPrice;
        this.firstBid = firstBid;
        this.totalBids = totalBids;
        this.highestBid = highestBid;
        this.bidder = bidder;
        this.categories = categories;
        this.images = images;
        this.address = address;
        this.expired = ((this.buyPrice != null && this.highestBid >= this.buyPrice) || (TimeManager.now().getTime() >= this.endDate.getTime()));
    }
}
