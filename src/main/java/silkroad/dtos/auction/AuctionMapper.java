package silkroad.dtos.auction;

import org.mapstruct.*;
import silkroad.dtos.auction.response.AuctionBasicDetails;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.dtos.auction.response.AuctionDto;
import silkroad.entities.Auction;
import silkroad.entities.Category;
import silkroad.entities.Image;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AuctionMapper {

    @Mapping(target = "sellerRating", source = "seller.sellerRating")
    @Mapping(target = "sellerUsername", source = "seller.username")
    @Mapping(target = "images", expression = "java(imagesToImagePaths(auction.getImages()))")
    AuctionCompleteDetails mapToAuctionCompleteDetails(Auction auction);

    @Mapping(source = "address.country", target = "country")
    @Mapping(target = "images", expression = "java(imagesToImagePaths(auction.getImages()))")
    AuctionBasicDetails mapToAuctionBasicDetails(Auction auction);


    List<AuctionBasicDetails> mapToAuctionsBasicDetails(List<Auction> auctions);


    @Mapping(source = "bidder.username", target = "bidder")
    @Mapping(target = "categories", expression = "java(categoriesToCategoryNames(auction.getCategories()))")
    @Mapping(target = "images", expression = "java(imagesToImagePaths(auction.getImages()))")
    AuctionDto mapToAuctionDto(Auction auction);

    List<AuctionDto> mapToAuctionsDtosDetails(List<Auction> auctions);

    default List<String> imagesToImagePaths(Set<Image> images) {
        return images.stream().map(Image::getPath).collect(Collectors.toList());
    }


    default Set<String> categoriesToCategoryNames(Set<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toSet());
    }

}
