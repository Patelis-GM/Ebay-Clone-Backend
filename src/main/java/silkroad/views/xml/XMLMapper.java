package silkroad.views.xml;

import org.mapstruct.*;
import silkroad.entities.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface XMLMapper {

    @Mapping(target = "longitude", source = "coordinates.longitude")
    @Mapping(target = "latitude", source = "coordinates.latitude")
    AddressXML map(Address address);

    SellerXML mapToSellerXML(User user);

    CategoryXML map(Category category);

    @Mapping(target = "location", source = "address.location")
    @Mapping(target = "country", source = "address.country")
    BidderXML mapToBidderXML(User user);

    BidXML map(Bid bid);

    @Mapping(target = "country", source = "address.country")
    AuctionXML map(Auction auction);

    List<AuctionXML> map(List<Auction> auctions);


}
