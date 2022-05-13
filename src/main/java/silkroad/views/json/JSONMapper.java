package silkroad.views.json;

import org.mapstruct.*;
import silkroad.entities.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface JSONMapper {

    String US_CURRENCY = "$";

    @Mapping(target = "country", source = "user.address.country")
    @Mapping(target = "location", source = "user.address.location")
    BidderJSON toBidderJSON(User user);

    @Mapping(target = "amount", expression = "java(mapCurrency(bid.getAmount(), US_CURRENCY))")
    BidJSON toBidJSON(Bid bid);

    List<BidJSON> toBidJSONList(List<Bid> bids);

    SellerJSON toSellerJSON(User user);

    @Mapping(target = "longitude", source = "coordinates.longitude")
    @Mapping(target = "latitude", source = "coordinates.latitude")
    AddressJSON toAddressJSON(Address address);


    @Mapping(target = "categories", expression = "java(mapCategories(auction.getCategories()))")
    @Mapping(target = "highestBid", expression = "java(mapCurrency(auction.getHighestBid(), US_CURRENCY))")
    @Mapping(target = "buyPrice", expression = "java(mapCurrency(auction.getBuyPrice(), US_CURRENCY))")
    @Mapping(target = "firstBid", expression = "java(mapCurrency(auction.getFirstBid(), US_CURRENCY))")
    AuctionJSON toAuctionJSON(Auction auction);

    List<AuctionJSON> toAuctionJSONList(List<Auction> auctions);

    default List<String> mapCategories(Set<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toList());
    }

    default String mapCurrency(Double amount, String currency) {
        if (amount == null || amount == 0)
            return null;
        return currency + amount;
    }
}
