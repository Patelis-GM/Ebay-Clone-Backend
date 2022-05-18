package silkroad.views.xml;

import org.mapstruct.*;
import silkroad.entities.*;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface XMLMapper {


    @Mapping(target = "longitude", source = "coordinates.longitude")
    @Mapping(target = "latitude", source = "coordinates.latitude")
    AddressXML toAddressXML(Address address);

    SellerXML toSellerXML(User user);

    @Mapping(target = "location", source = "address.location")
    @Mapping(target = "country", source = "address.country")
    BidderXML toBidderXML(User user);

    @Mapping(target = "amount", expression = "java(mapCurrency(bid.getAmount(), currency))")
    BidXML toBidXML(Bid bid, @Context String currency);

    CategoryXML toCategoryXML(Category category);

    List<CategoryXML> toCategoryXMLList(Set<Category> categories);

    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "highestBid", expression = "java(mapCurrency(auction.getHighestBid(), currency))")
    @Mapping(target = "buyPrice", expression = "java(mapCurrency(auction.getBuyPrice(), currency))")
    @Mapping(target = "firstBid", expression = "java(mapCurrency(auction.getFirstBid(), currency))")
    AuctionXML toAuctionXML(Auction auction, @Context String currency);

    List<AuctionXML> toAuctionXMLList(List<Auction> auctions, @Context String currency);


    default String mapCurrency(Double amount, @Context String currency) {
        if (amount == null || amount == 0)
            return null;
        return currency + amount;
    }

}
