package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import silkroad.dtos.auction.request.AuctionPosting;
import silkroad.dtos.auction.response.AuctionBrowsingDetails;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.dtos.bid.request.BidPosting;
import silkroad.dtos.bid.response.BidSellerDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.dtos.user.request.Username;
import silkroad.entities.Address;
import silkroad.services.AddressService;
import silkroad.services.AuctionService;
import silkroad.services.BidService;


@AllArgsConstructor
@RestController
public class AuctionController {

    private final AddressService addressService;
    private final AuctionService auctionService;
    private final BidService bidService;

    /* Create Auction */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/auctions", method = RequestMethod.POST)
    public ResponseEntity<Void> createAuction(Authentication authentication, @RequestPart(name = "auction") AuctionPosting auctionDTO, @RequestPart(name = "images") MultipartFile[] multipartFiles) {
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.createAuction(authentication, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /* Update Auction */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/auctions/{auctionID}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAuction(Authentication authentication, @PathVariable Long auctionID, @RequestPart(name = "auction") AuctionPosting auctionDTO, @RequestPart(name = "images") MultipartFile[] multipartFiles) {
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.updateAuction(authentication, auctionID, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Delete Auction */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/auctions/{auctionID}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAuction(Authentication authentication, @PathVariable Long auctionID) {
        this.auctionService.deleteAuction(authentication, auctionID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Get Auction */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/auctions/{auctionID}", method = RequestMethod.GET)
    public ResponseEntity<AuctionCompleteDetails> getAuction(Authentication authentication, @PathVariable Long auctionID) {
        return new ResponseEntity<>(this.auctionService.getAuction(authentication, auctionID), HttpStatus.OK);
    }


    /* Browse Auctions */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/auctions", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionBrowsingDetails>> browseAuctions(@RequestParam(name = "query", required = false) String textSearch,
                                                                               @RequestParam(name = "min-price", required = false) Double minPrice,
                                                                               @RequestParam(name = "max-price", required = false) Double maxPrice,
                                                                               @RequestParam(name = "location", required = false) String location,
                                                                               @RequestParam(name = "category", required = false) String category,
                                                                               @RequestParam(name = "buy-now", required = false) Boolean hasBuyPrice,
                                                                               @RequestParam(name = "page") Integer pageIndex,
                                                                               @RequestParam(name = "size") Integer pageSize) {

        return new ResponseEntity<>(this.auctionService.browseAuctions(pageIndex - 1, pageSize, textSearch, minPrice, maxPrice, category, location, hasBuyPrice), HttpStatus.OK);
    }


    /* Bid on Auction */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/auctions/{auctionID}/bid", method = RequestMethod.POST)
    public ResponseEntity<Void> bid(Authentication authentication, @PathVariable Long auctionID, @RequestBody BidPosting bidPosting) {
        this.bidService.bid(authentication, auctionID, bidPosting.getAmount(), bidPosting.getVersion());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /* Get Auction Bids */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/auctions/{auctionID}/bid", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<BidSellerDetails>> getAuctionBids(Authentication authentication,
                                                                         @PathVariable Long auctionID,
                                                                         @RequestParam(name = "page") Integer pageIndex,
                                                                         @RequestParam("size") Integer pageSize) {
        return new ResponseEntity<>(this.bidService.getAuctionBids(authentication, auctionID, pageIndex - 1, pageSize), HttpStatus.OK);
    }


}
