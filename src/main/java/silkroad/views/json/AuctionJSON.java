package silkroad.views.json;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@JsonPropertyOrder({"id", "name", "categories", "highestBid", "buyPrice", "firstBid", "totalBids", "bids", "address", "startDate", "endDate", "seller", "description"})
public class AuctionJSON implements Serializable {

    public AuctionJSON(Long id, String name, List<String> categories, String highestBid, String buyPrice, String firstBid, Long totalBids, List<BidJSON> bids, AddressJSON address, SellerJSON seller, Date startDate, Date endDate, String description) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.buyPrice = buyPrice;
        this.firstBid = firstBid;
        this.totalBids = totalBids;
        this.highestBid = (highestBid == null ? this.firstBid : highestBid);
        this.bids = bids;
        this.address = address;
        this.seller = seller;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    @JsonProperty("ItemID")
    private final Long id;

    @JsonProperty("Name")
    private final String name;

    @JsonProperty("Categories")
    private final List<String> categories;

    @JsonProperty("Currently")
    private final String highestBid;

    @JsonProperty("Buy_Price")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String buyPrice;

    @JsonProperty("First_Bid")
    private final String firstBid;

    @JsonProperty("Number_of_Bids")
    private final Long totalBids;

    @JsonProperty("Bids")
    private final List<BidJSON> bids;

    @JsonUnwrapped
    private final AddressJSON address;

    @JsonProperty("Seller")
    private final SellerJSON seller;

    @JsonProperty("Started")
    @JsonFormat(pattern = "MMM-dd-yy HH:mm:ss")
    private final Date startDate;

    @JsonProperty("Ends")
    @JsonFormat(pattern = "MMM-dd-yy HH:mm:ss")
    private final Date endDate;

    @JsonProperty("Description")
    private final String description;
}
