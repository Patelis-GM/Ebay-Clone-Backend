package silkroad.dtos.bid;

import org.mapstruct.*;
import silkroad.dtos.bid.response.BidBuyerDetails;
import silkroad.dtos.bid.response.BidSellerDetails;
import silkroad.entities.Bid;
import silkroad.entities.Image;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BidMapper {


    @Mapping(target = "bidder", source = "bidder.username")
    @Mapping(target = "bidderRating", source = "bidder.buyerRating")
    BidSellerDetails toBidSellerDetails(Bid bid);

    List<BidSellerDetails> toBidSellerDetailsList(List<Bid> bids);

    @Mapping(target = "auctionName", source = "bid.auction.name")
    @Mapping(target = "auctionID", source = "bid.auction.id")
    @Mapping(target = "images", expression = "java(mapImages(bid.getAuction().getImages()))")
    BidBuyerDetails toBidBuyerDetails(Bid bid);

    List<BidBuyerDetails> toBidBuyerDetailsList(List<Bid> bids);


    default List<String> mapImages(List<Image> images) {
        return images.stream().map(Image::getPath).collect(Collectors.toList());
    }


}
