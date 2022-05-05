package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import silkroad.dtos.auction.request.AuctionPosting;
import silkroad.dtos.auction.response.AuctionBrowsingBasicDetails;
import silkroad.dtos.auction.response.AuctionBrowsingCompleteDetails;
import silkroad.dtos.bid.response.BidDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.entities.Address;
import silkroad.services.AddressService;
import silkroad.services.AuctionService;
import silkroad.services.BidService;

import java.util.Collection;

@AllArgsConstructor
@RestController
public class AuctionController {

    private final AddressService addressService;
    private final AuctionService auctionService;
    private final BidService bidService;

    /* Create Auction */
    @RequestMapping(value = "/auctions", method = RequestMethod.POST)
    public ResponseEntity<Void> createAuction(Authentication authentication, @RequestPart(name = "auction") AuctionPosting auctionDTO, @RequestPart(name = "images") MultipartFile[] multipartFiles) {
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.createAuction(authentication, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Update Auction */
    @RequestMapping(value = "/auctions/{auctionID}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAuction(Authentication authentication, @PathVariable Long auctionID, @RequestPart(name = "auction") AuctionPosting auctionDTO, @RequestPart(name = "images") MultipartFile[] multipartFiles) {
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.updateAuction(authentication.getName(), auctionID, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Delete Auction */
    @RequestMapping(value = "/auctions/{auctionID}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAuction(Authentication authentication, @PathVariable Long auctionID) {
        this.auctionService.deleteAuction(authentication.getName(), auctionID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Get Auction */
    @RequestMapping(value = "/auctions/{auctionID}", method = RequestMethod.GET)
    public ResponseEntity<AuctionBrowsingCompleteDetails> getAuction(Authentication authentication, @PathVariable Long auctionID) {
        System.out.println(authentication);
        return new ResponseEntity<>(this.auctionService.getAuction(authentication, auctionID), HttpStatus.OK);
    }

    /* Get Auctions */
    @RequestMapping(value = "/auctions", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionBrowsingBasicDetails>> browseAuctions(@RequestParam(name = "query", required = false) String textSearch,
                                                                                    @RequestParam(name = "min-price", required = false) Double minPrice,
                                                                                    @RequestParam(name = "max-price", required = false) Double maxPrice,
                                                                                    @RequestParam(name = "location", required = false) String location,
                                                                                    @RequestParam(name = "category", required = false) String category,
                                                                                    @RequestParam(name = "buy-now", required = false) Boolean hasBuyPrice,
                                                                                    @RequestParam(name = "page") Integer pageIndex,
                                                                                    @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.auctionService.getAuctions(pageIndex - 1, pageSize, textSearch, minPrice, maxPrice, category, location, hasBuyPrice), HttpStatus.OK);
    }


    /* Bid on Auction */
    @RequestMapping(value = "/auctions/{auctionID}/bid", method = RequestMethod.POST)
    public ResponseEntity<Void> bid(Authentication authentication, @PathVariable Long auctionID, @RequestParam(name = "amount") Double amount) {
        this.bidService.bid(authentication.getName(), auctionID, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Get Auction Bids */
    @RequestMapping(value = "/auctions/{auctionID}/bid", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<BidDetails>> getBids(@PathVariable Long auctionID,
                                                            @RequestParam(name = "page") Integer pageIndex,
                                                            @RequestParam("size") Integer pageSize,
                                                            @RequestParam(value = "sort-by", required = false) String sortField,
                                                            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        return new ResponseEntity<>(this.bidService.getBids(auctionID, pageIndex - 1, pageSize, sortField, sortDirection), HttpStatus.OK);
    }




    //    <-------------------------------- Testing -------------------------------->
    @RequestMapping(value = "/create1", method = RequestMethod.POST)
    public ResponseEntity<Void> createAuction1(Authentication authentication, @RequestBody AuctionPosting auctionDTO) {
        MultipartFile[] multipartFiles = new MultipartFile[5];
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        Authentication authentication1 = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "USER1";
            }
        };
        this.auctionService.createAuction(authentication1, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update1", method = RequestMethod.POST)
    public ResponseEntity<Void> updateAuction1(@RequestParam Long auctionID, Authentication authentication, @RequestBody AuctionPosting auctionDTO) {
        MultipartFile[] multipartFiles = new MultipartFile[5];
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.updateAuction("user1", auctionID, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete1", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteAuction1(Authentication authentication, @RequestParam Long auctionID) {
        this.auctionService.deleteAuction("user1", auctionID);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
