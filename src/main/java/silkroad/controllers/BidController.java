package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping(value = "/bid1", method = RequestMethod.POST)
    public ResponseEntity<Void> bid1(Authentication authentication, @RequestParam(name = "id") Long auctionID, @RequestParam(name = "amount") Double amount) {
        this.bidService.bid("user2", auctionID, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
