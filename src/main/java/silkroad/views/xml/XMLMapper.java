package silkroad.views.xml;

import org.mapstruct.*;
import silkroad.entities.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface XMLMapper {

    String US_CURRENCY = "$";

    @Mapping(target = "longitude", source = "coordinates.longitude")
    @Mapping(target = "latitude", source = "coordinates.latitude")
    AddressXML toAddressXML(Address address);

    SellerXML toSellerXML(User user);

    @Mapping(target = "location", source = "address.location")
    @Mapping(target = "country", source = "address.country")
    BidderXML toBidderXML(User user);

    @Mapping(target = "amount", expression = "java(mapCurrency(bid.getAmount(), US_CURRENCY))")
    BidXML toBidXML(Bid bid);

    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "highestBid", expression = "java(mapCurrency(auction.getHighestBid(), US_CURRENCY))")
    @Mapping(target = "buyPrice", expression = "java(mapCurrency(auction.getBuyPrice(), US_CURRENCY))")
    @Mapping(target = "firstBid", expression = "java(mapCurrency(auction.getFirstBid(), US_CURRENCY))")
    AuctionXML toAuctionXML(Auction auction);

    List<AuctionXML> toAuctionXMLList(List<Auction> auctions);

    CategoryXML toCategoryXML(Category category);

    default String mapCurrency(Double amount, String currency) {
        if (amount == null || amount == 0)
            return null;
        return currency + amount;
    }

}
