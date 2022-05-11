package silkroad.dtos.auction;

import org.mapstruct.*;
import silkroad.dtos.auction.response.AuctionBrowsingBasicDetails;
import silkroad.dtos.auction.response.AuctionBrowsingCompleteDetails;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.dtos.auction.response.AuctionPurchaseDetails;
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
    @Mapping(target = "images", expression = "java(mapImages(auction.getImages()))")
    AuctionBrowsingCompleteDetails toAuctionBrowsingCompleteDetails(Auction auction);

    @Mapping(source = "address.country", target = "country")
    @Mapping(target = "images", expression = "java(mapImages(auction.getImages()))")
    AuctionBrowsingBasicDetails toAuctionBrowsingBasicDetails(Auction auction);

    List<AuctionBrowsingBasicDetails> toAuctionBrowsingBasicDetailsList(List<Auction> auctions);

    @Mapping(source = "latestBid.bidder.username", target = "bidder")
    @Mapping(target = "categories", expression = "java(mapCategories(auction.getCategories()))")
    @Mapping(target = "images", expression = "java(mapImages(auction.getImages()))")
    AuctionCompleteDetails toAuctionCompleteDetails(Auction auction);

    List<AuctionCompleteDetails> toAuctionCompleteDetailsDetailsList(List<Auction> auctions);

    @Mapping(target = "date", source = "latestBid.submissionDate")
    @Mapping(target = "cost", source = "latestBid.amount")
    @Mapping(target = "seller", source = "seller.username")
    @Mapping(target = "images", expression = "java(mapImages(auction.getImages()))")
    AuctionPurchaseDetails toAuctionPurchaseDetails(Auction auction);

    List<AuctionPurchaseDetails> toAuctionPurchaseDetailsList(List<Auction> auctions);

    default List<String> mapImages(List<Image> images) {
        return images.stream().map(Image::getPath).collect(Collectors.toList());
    }

    default Set<String> mapCategories(Set<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toSet());
    }


}
