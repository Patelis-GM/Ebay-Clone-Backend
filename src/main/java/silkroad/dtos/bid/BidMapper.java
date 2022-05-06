package silkroad.dtos.bid;

import org.mapstruct.*;
import silkroad.dtos.bid.response.BidBuyerDetails;
import silkroad.dtos.bid.response.BidSellerDetails;
import silkroad.entities.Bid;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BidMapper {


    @Mapping(target = "bidder", source = "bidder.username")
    @Mapping(target = "bidderRating", source = "bidder.buyerRating")
    BidSellerDetails mapBidToBidSellerDetails(Bid bid);

    List<BidSellerDetails> mapBidsToBidSellerDetailsList(List<Bid> bids);

    @Mapping(target = "auctionName", source = "bid.auction.name")
    @Mapping(target = "auctionID", source = "bid.auction.id")
    BidBuyerDetails bidToBidBuyerDetails(Bid bid);

    List<BidBuyerDetails> mapBidsToBidBuyerDetailsList(List<Bid> bids);


}