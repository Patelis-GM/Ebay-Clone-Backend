package silkroad.views.json;

import org.mapstruct.*;
import silkroad.entities.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface JSONMapper {


    @Mapping(target = "country", source = "user.address.country")
    @Mapping(target = "location", source = "user.address.location")
    BidderJSON toBidderJSON(User user);

    @Mapping(target = "amount", expression = "java(mapCurrency(bid.getAmount(), currency))")
    BidJSON toBidJSON(Bid bid, @Context String currency);

    SellerJSON toSellerJSON(User user);

    @Mapping(target = "longitude", source = "coordinates.longitude")
    @Mapping(target = "latitude", source = "coordinates.latitude")
    AddressJSON toAddressJSON(Address address);

    @Mapping(target = "categories", expression = "java(mapCategories(auction.getCategories()))")
    @Mapping(target = "highestBid", expression = "java(mapCurrency(auction.getHighestBid(), currency))")
    @Mapping(target = "buyPrice", expression = "java(mapCurrency(auction.getBuyPrice(), currency))")
    @Mapping(target = "firstBid", expression = "java(mapCurrency(auction.getFirstBid(), currency))")
    AuctionJSON toAuctionJSON(Auction auction, @Context String currency);

    List<AuctionJSON> toAuctionJSONList(List<Auction> auctions, @Context String currency);

    default List<String> mapCategories(Set<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toList());
    }

    default String mapCurrency(Double amount, String currency) {
        if (amount == null || amount == 0)
            return null;
        return currency + amount;
    }
}
