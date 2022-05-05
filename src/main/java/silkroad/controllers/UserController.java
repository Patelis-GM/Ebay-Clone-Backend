package silkroad.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.dtos.auction.response.AuctionPurchaseDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.services.AuctionService;

@AllArgsConstructor
@RestController
public class UserController {

    private final AuctionService auctionService;


    @RequestMapping(value = "/users/{username}/auctions", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionCompleteDetails>> userAuctions(Authentication authentication,
                                                                             @PathVariable String username,
                                                                             @RequestParam(name = "sold", required = false) Boolean sold,
                                                                             @RequestParam(name = "active", required = false) Boolean active,
                                                                             @RequestParam(name = "page") Integer pageIndex,
                                                                             @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.auctionService.getUserAuctions(authentication, username, pageIndex - 1, pageSize, sold, active), HttpStatus.OK);
    }


    @RequestMapping(value = "/users/{username}/purchases", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionPurchaseDetails>> userPurchases(Authentication authentication,
                                                                              @PathVariable String username,
                                                                              @RequestParam(name = "sort-field", required = false) String sortField,
                                                                              @RequestParam(name = "sort-direction", required = false) String sortDirection,
                                                                              @RequestParam(name = "page") Integer pageIndex,
                                                                              @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.auctionService.getUserPurchases(authentication, username, pageIndex - 1, pageSize, sortField, sortDirection), HttpStatus.OK);
    }


}
