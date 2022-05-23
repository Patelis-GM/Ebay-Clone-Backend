package silkroad.dtos.bid;

import org.mapstruct.*;
import silkroad.dtos.bid.response.BidBuyerDetails;
import silkroad.dtos.bid.response.BidSellerDetails;
import silkroad.entities.Bid;
import silkroad.entities.Image;

import java.util.ArrayList;
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

        final String SLASH = "/";

        List<String> imageList = new ArrayList<>();
        for (Image image : images) {
            String filename = image.getPath().substring(image.getPath().lastIndexOf(SLASH) + 1);
            imageList.add(filename);
        }

        return imageList;
    }


}
