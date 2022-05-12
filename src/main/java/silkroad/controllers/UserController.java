package silkroad.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.dtos.auction.response.AuctionPurchaseDetails;
import silkroad.dtos.bid.response.BidBuyerDetails;
import silkroad.dtos.message.request.MessagePosting;
import silkroad.dtos.page.PageResponse;
import silkroad.dtos.user.request.UserRegistration;
import silkroad.entities.Address;
import silkroad.exceptions.UserException;
import silkroad.services.*;

@AllArgsConstructor
@RestController
public class UserController {

    private final AuctionService auctionService;
    private final BidService bidService;
    private final UserService userService;
    private final AddressService addressService;
    private final MessageService messageService;


    /* Create User */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Void> signUp(@RequestBody UserRegistration user) {
        Address address = this.addressService.createOrFindAddress(user.getAddress());
        user.setAddress(address);
        this.userService.signUpUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{username}/auctions", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionCompleteDetails>> getUserPostedAuctions(Authentication authentication,
                                                                                      @PathVariable String username,
                                                                                      @RequestParam(name = "sold", required = false) Boolean sold,
                                                                                      @RequestParam(name = "active", required = false) Boolean active,
                                                                                      @RequestParam(name = "page") Integer pageIndex,
                                                                                      @RequestParam(name = "size") Integer pageSize) {
        UserException.validateAuthentication(authentication, username);
        return new ResponseEntity<>(this.auctionService.getUserPostedAuctions(authentication, pageIndex - 1, pageSize, active, sold), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{username}/purchases", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionPurchaseDetails>> getUserPurchasedAuctions(Authentication authentication,
                                                                                         @PathVariable String username,
                                                                                         @RequestParam(name = "page") Integer pageIndex,
                                                                                         @RequestParam(name = "size") Integer pageSize) {
        UserException.validateAuthentication(authentication, username);
        return new ResponseEntity<>(this.auctionService.getUserPurchasedAuctions(authentication, pageIndex - 1, pageSize), HttpStatus.OK);
    }


    @RequestMapping(value = "/users/{username}/bids", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<BidBuyerDetails>> getUserBids(Authentication authentication,
                                                                     @PathVariable String username,
                                                                     @RequestParam(name = "page") Integer pageIndex,
                                                                     @RequestParam(name = "size") Integer pageSize) {
        UserException.validateAuthentication(authentication, username);
        return new ResponseEntity<>(this.bidService.getUserBids(authentication, pageIndex - 1, pageSize), HttpStatus.OK);
    }

    /* Send Message */
    @RequestMapping(value = "/users/{username}/messages", method = RequestMethod.POST)
    public ResponseEntity<Void> sendMessage(Authentication authentication,
                                            @PathVariable String username,
                                            @RequestBody MessagePosting messagePosting) {
        UserException.validateAuthentication(authentication, username);
        this.messageService.sendMessage(authentication, messagePosting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /* Inbox - Outbox */
    @RequestMapping(value = "/users/{username}/messages", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<?>> getUserMessages(Authentication authentication,
                                                           @PathVariable String username,
                                                           @RequestParam(name = "sent") Boolean sent,
                                                           @RequestParam(name = "page") Integer pageIndex,
                                                           @RequestParam(name = "size") Integer pageSize) {
        UserException.validateAuthentication(authentication, username);
        if (sent)
            return new ResponseEntity<>(this.messageService.getUserSentMessages(authentication, pageIndex - 1, pageSize), HttpStatus.OK);
        else
            return new ResponseEntity<>(this.messageService.getUserReceivedMessages(authentication, pageIndex - 1, pageSize), HttpStatus.OK);
    }

    /* Delete Message Access */
    @RequestMapping(value = "/users/{username}/messages/{messageID}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUserMessage(Authentication authentication,
                                                  @PathVariable String username,
                                                  @PathVariable Long messageID) {
        UserException.validateAuthentication(authentication, username);
        this.messageService.deleteUserMessage(authentication, messageID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Read Message */
    @RequestMapping(value = "/users/{username}/messages/{messageID}", method = RequestMethod.PUT)
    public ResponseEntity<Void> readUserMessage(Authentication authentication,
                                                @PathVariable String username,
                                                @PathVariable Long messageID) {
        UserException.validateAuthentication(authentication, username);
        this.messageService.readUserMessage(authentication, messageID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
