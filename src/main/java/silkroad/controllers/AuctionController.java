package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import silkroad.dtos.auction.request.AuctionPosting;
import silkroad.dtos.auction.response.AuctionBasicDetails;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.dtos.auction.response.AuctionDto;
import silkroad.dtos.page.PageResponse;
import silkroad.entities.Address;
import silkroad.services.AddressService;
import silkroad.services.AuctionService;

@AllArgsConstructor
@RestController
public class AuctionController {

    private final AddressService addressService;
    private final AuctionService auctionService;


    @RequestMapping(value = "/create-auction", method = RequestMethod.POST)
    public ResponseEntity<Void> createAuction(Authentication authentication, @RequestPart(name = "auction") AuctionPosting auctionDTO, @RequestPart(name = "images") MultipartFile[] multipartFiles) {
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.createAuction(authentication.getName(), auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update-action", method = RequestMethod.POST)
    public ResponseEntity<Void> updateAuction(Authentication authentication, @RequestParam(name = "id") Long auctionID, @RequestPart(name = "auction") AuctionPosting auctionDTO, @RequestPart(name = "images") MultipartFile[] multipartFiles) {
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.updateAuction(authentication.getName(), auctionID, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete-auction", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteAuction(Authentication authentication, @RequestParam(name = "id") Long auctionID) {
        this.auctionService.deleteAuction(authentication.getName(), auctionID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{auctionID}", method = RequestMethod.GET)
    public ResponseEntity<AuctionCompleteDetails> getAuction(Authentication authentication, @PathVariable Long auctionID) {
        System.out.println(authentication);
        return new ResponseEntity<>(this.auctionService.getAuction(authentication, auctionID), HttpStatus.OK);
    }

    @RequestMapping(value = "/browse", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionBasicDetails>> browseAuctions(@RequestParam(name = "query", required = false) String textSearch,
                                                                            @RequestParam(name = "min-price", required = false) Double minPrice,
                                                                            @RequestParam(name = "max-price", required = false) Double maxPrice,
                                                                            @RequestParam(name = "location", required = false) String location,
                                                                            @RequestParam(name = "category", required = false) String category,
                                                                            @RequestParam(name = "buy-now", required = false) Boolean hasBuyPrice,
                                                                            @RequestParam(name = "page") Integer pageIndex,
                                                                            @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.auctionService.getAuctions(pageIndex - 1, pageSize, textSearch, minPrice, maxPrice, category, location, hasBuyPrice), HttpStatus.OK);
    }


    @RequestMapping(value = "/my-auctions", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<AuctionDto>> myAuctions(Authentication authentication,
                                                               @RequestParam(name = "sold", required = false) Boolean sold,
                                                               @RequestParam(name = "page") Integer pageIndex,
                                                               @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.auctionService.getUserAuctions(authentication, pageIndex - 1, pageSize, sold), HttpStatus.OK);
    }


    @RequestMapping(value = "/create1", method = RequestMethod.POST)
    public ResponseEntity<Void> createAuction1(Authentication authentication, @RequestBody AuctionPosting auctionDTO) {
        MultipartFile[] multipartFiles = new MultipartFile[5];
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.createAuction("user1", auctionDTO, multipartFiles);
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
