package silkroad.dtos.bid;

import org.mapstruct.*;
import silkroad.dtos.bid.response.BidDetails;
import silkroad.entities.Bid;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BidMapper {


    @Mapping(target = "bidder", source = "bidder.username")
    @Mapping(target = "bidderRating", source = "bidder.buyerRating")
    BidDetails mapBidToBidDetails(Bid bid);

    List<BidDetails> mapBidToBidsDetails(List<Bid> content);


}
