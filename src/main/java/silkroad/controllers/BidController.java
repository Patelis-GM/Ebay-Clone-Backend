package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import silkroad.dtos.bid.response.BidDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.services.BidService;

@RestController
@AllArgsConstructor
public class BidController {

    private final BidService bidService;

    @RequestMapping(value = "/bid", method = RequestMethod.POST)
    public ResponseEntity<Void> bid(Authentication authentication, @RequestParam(name = "id") Long auctionID, @RequestParam(name = "amount") Double amount) {
        this.bidService.bid(authentication.getName(), auctionID, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bids/{auctionID}", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<BidDetails>> getBids(@PathVariable Long auctionID,
                                                            @RequestParam(name = "page") Integer pageIndex,
                                                            @RequestParam("size") Integer pageSize,
                                                            @RequestParam(value = "sort-by", required = false) String sortField,
                                                            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        return new ResponseEntity<>(this.bidService.getBids(auctionID, pageIndex - 1, pageSize, sortField, sortDirection), HttpStatus.OK);
    }


    @RequestMapping(value = "/bid1", method = RequestMethod.POST)
    public ResponseEntity<Void> bid1(Authentication authentication, @RequestParam(name = "id") Long auctionID, @RequestParam(name = "amount") Double amount) {
        this.bidService.bid("user2", auctionID, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
