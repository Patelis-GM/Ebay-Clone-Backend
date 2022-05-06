package silkroad.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.dtos.auction.response.AuctionPurchaseDetails;
import silkroad.dtos.bid.response.BidBuyerDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.services.AuctionService;
import silkroad.services.BidService;

@AllArgsConstructor
@RestController
public class UserController {

    private final AuctionService auctionService;
    private final BidService bidService;


    @RequestMapping(value = "/users/{username}/auctions", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionCompleteDetails>> getUserPostedAuctions(Authentication authentication,
                                                                             @PathVariable String username,
                                                                             @RequestParam(name = "sold", required = false) Boolean sold,
                                                                             @RequestParam(name = "active", required = false) Boolean active,
                                                                             @RequestParam(name = "page") Integer pageIndex,
                                                                             @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.auctionService.getUserPostedAuctions(authentication, username, pageIndex - 1, pageSize, active, sold), HttpStatus.OK);
    }


    @RequestMapping(value = "/users/{username}/purchases", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionPurchaseDetails>> getUserPurchasedAuctions(Authentication authentication,
                                                                              @PathVariable String username,
                                                                              @RequestParam(name = "page") Integer pageIndex,
                                                                              @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.auctionService.getUserPurchasedAuctions(authentication, username, pageIndex - 1, pageSize), HttpStatus.OK);
    }


    @RequestMapping(value = "/users/{username}/bids", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<BidBuyerDetails>> getUserBids(Authentication authentication,
                                                                     @PathVariable String username,
                                                                     @RequestParam(name = "page") Integer pageIndex,
                                                                     @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.bidService.getUserBids(authentication, username, pageIndex - 1, pageSize), HttpStatus.OK);
    }


}
