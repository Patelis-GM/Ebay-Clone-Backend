package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import silkroad.dtos.auction.request.AuctionStoreDTO;
import silkroad.entities.Address;
import silkroad.entities.Auction;
import silkroad.services.AddressService;
import silkroad.services.AuctionService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/auctions")
public class AuctionController {

    private final AddressService addressService;
    private final AuctionService auctionService;


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createAuction(Authentication authentication, @RequestPart(name = "auction") AuctionStoreDTO auctionDTO, @RequestPart(name = "images") MultipartFile[] multipartFiles) {
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.createAuction(authentication.getName(), auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<Void> updateAuction(Authentication authentication, @RequestParam(name = "id") Long auctionID, @RequestPart(name = "auction") AuctionStoreDTO auctionDTO, @RequestPart(name = "images") MultipartFile[] multipartFiles) {
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.updateAuction(authentication.getName(), auctionID, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteAuction(Authentication authentication, @RequestParam(name = "id") Long auctionID) {
        this.auctionService.deleteAuction(authentication.getName(), auctionID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/create1", method = RequestMethod.POST)
    public ResponseEntity<Void> createAuction1(Authentication authentication, @RequestBody AuctionStoreDTO auctionDTO) {
        MultipartFile[] multipartFiles = new MultipartFile[5];
        Address address = this.addressService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        this.auctionService.createAuction("user1", auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update1", method = RequestMethod.POST)
    public ResponseEntity<Void> updateAuction1(@RequestParam Long auctionID, Authentication authentication, @RequestBody AuctionStoreDTO auctionDTO) {
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




//    @RequestMapping(value = "/search", method = RequestMethod.POST)
//    public ResponseEntity<Void> searchAuctions(@RequestParam(name = "query" ,required = false) String query
//            ,@RequestParam(name = "category",required = false) String category,@RequestParam(name = "minPrice",required = false) Double minPrice,Double  ) {
//        this.auctionService.deleteAuction(authentication.getName(), auctionID);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @RequestMapping(value = "/q", method = RequestMethod.GET)
    public ResponseEntity<Void> deleteAuction() {
        List<Auction> auctions = this.auctionService.f();
        System.out.println("IN HERE BUD");
        System.out.println(auctions.size());
        for (Auction auction : auctions)
            System.out.println(auction);
        return new ResponseEntity<>(HttpStatus.OK);
    }


//


//    @RequestMapping(value = "/{auctionID}", method = RequestMethod.GET)
//    public ResponseEntity<Void> getAuction(Authentication authentication, Long auctionID) {
//        return new ResponseEntity<>(this.auctionService.getAuction(authentication,auctionID),HttpStatus.OK);
//    }
//

}
