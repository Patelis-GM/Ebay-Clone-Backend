package silkroad.dtos.auction.response;

import lombok.Getter;
import silkroad.entities.Address;
import silkroad.utilities.TimeManager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
public class AuctionBrowsingCompleteDetails implements Serializable {

    private final Long id;
    private final String name;
    private final String description;
    private final Date endDate;
    private final Double buyPrice;
    private final Double firstBid;
    private final Long totalBids;
    private final Double highestBid;
    private final String sellerUsername;
    private final Double sellerRating;
    private final List<String> images;
    private final Address address;
    private final Boolean expired;
    private final Long version;
    private final Set<String> categories;


    public AuctionBrowsingCompleteDetails(Long id, String name, String description, Date endDate, Double buyPrice, Double firstBid, Long totalBids, Double highestBid, String sellerUsername, Double sellerRating, List<String> images, Address address, Long version, Set<String> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.buyPrice = buyPrice;
        this.firstBid = firstBid;
        this.totalBids = totalBids;
        this.highestBid = highestBid;
        this.sellerUsername = sellerUsername;
        this.sellerRating = sellerRating;
        this.images = images;
        this.address = address;
        this.expired = ((TimeManager.now().getTime() >= this.endDate.getTime()) || (this.buyPrice != null && this.highestBid >= this.buyPrice));
        this.version = version;
        this.categories = categories;
    }
}
