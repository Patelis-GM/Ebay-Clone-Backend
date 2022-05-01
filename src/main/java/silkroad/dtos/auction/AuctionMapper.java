package silkroad.dtos.auction;

import org.mapstruct.*;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.entities.Auction;
import silkroad.entities.Image;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AuctionMapper {

    @Mapping(target = "sellerRating", source = "seller.sellerRating")
    @Mapping(target = "sellerUsername", source = "seller.username")
    @Mapping(target = "images", expression = "java(imagesToImagePaths(auction.getImages()))")
    AuctionCompleteDetails mapToAuctionCompleteDetails(Auction auction);

    default List<String> imagesToImagePaths(List<Image> images) {
        return images.stream().map(Image::getPath).collect(Collectors.toList());
    }
}
