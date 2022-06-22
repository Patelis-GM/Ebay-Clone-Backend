package silkroad.dtos.auction;

import org.mapstruct.*;
import silkroad.dtos.auction.response.*;
import silkroad.entities.Auction;
import silkroad.entities.Category;
import silkroad.entities.Image;
import silkroad.utilities.TimeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AuctionMapper {

    @Mapping(source = "address.country", target = "country")
    @Mapping(target = "images", expression = "java(mapImages(auction.getImages()))")
    AuctionBrowsingDetails toAuctionBrowsingDetails(Auction auction);

    List<AuctionBrowsingDetails> toAuctionBrowsingDetailsList(List<Auction> auctions);


    @Mapping(target = "date", source = "latestBid.submissionDate")
    @Mapping(target = "cost", source = "latestBid.amount")
    @Mapping(target = "seller", source = "seller.username")
    @Mapping(target = "images", expression = "java(mapImages(auction.getImages()))")
    AuctionPurchaseDetails toAuctionPurchaseDetails(Auction auction);

    List<AuctionPurchaseDetails> toAuctionPurchaseDetailsList(List<Auction> auctions);


    @Mapping(target = "bidder", source = "latestBid.bidder.username")
    @Mapping(target = "images", expression = "java(mapImages(auction.getImages()))")
    @Mapping(target = "expired", expression = "java(mapExpired(auction))")
    AuctionBasicDetails toAuctionBasicDetails(Auction auction);

    List<AuctionBasicDetails> toAuctionBasicDetailsList(List<Auction> auctions);


    @Mapping(target = "sellerRating", source = "seller.sellerRating")
    @Mapping(target = "seller", source = "seller.username")
    @Mapping(target = "images", expression = "java(mapImages(auction.getImages()))")
    @Mapping(target = "categories", expression = "java(mapCategories(auction.getCategories()))")
    @Mapping(target = "expired", expression = "java(mapExpired(auction))")
    AuctionCompleteDetails toAuctionCompleteDetails(Auction auction);


    default List<String> mapImages(List<Image> images) {

        final String SLASH = "/";

        List<String> imageList = new ArrayList<>();
        for (Image image : images) {
            String filename = image.getPath().substring(image.getPath().lastIndexOf(SLASH) + 1);
            imageList.add(filename);
        }
        return imageList;
    }


    default List<String> mapCategories(Set<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toList());
    }

    default Boolean mapExpired(Auction auction) {
        return ((TimeManager.now().getTime() >= auction.getEndDate().getTime()) || (auction.getBuyPrice() != null && auction.getHighestBid() >= auction.getBuyPrice()));
    }

}
