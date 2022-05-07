package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import silkroad.dtos.auction.request.AuctionPosting;
import silkroad.entities.Address;
import silkroad.services.TestService;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class TestController {

    private final TestService testService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createAuctionTest(@RequestBody AuctionPosting auctionDTO) {
        MultipartFile[] multipartFiles = new MultipartFile[5];
        Address address = this.testService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        String username = "Doris";
        this.testService.createAuction(username, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<Void> updateAuctionTest(@RequestParam Long auctionID, @RequestBody AuctionPosting auctionDTO) {
        MultipartFile[] multipartFiles = new MultipartFile[5];
        Address address = this.testService.createOrFindAddress(auctionDTO.getAddress());
        auctionDTO.setAddress(address);
        String username = "Doris";
        this.testService.updateAuction(username, auctionID, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteAuctionTest(@RequestParam(name = "id") Long auctionID) {
        String username = "Doris";
        this.testService.deleteAuction(username, auctionID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bid", method = RequestMethod.POST)
    public ResponseEntity<Void> bidTest(@RequestParam(name = "id") Long auctionID, @RequestParam(name = "amount") Double amount, @RequestParam(name = "version") Long version) {
        String username = "Doris";
        this.testService.bid(username, auctionID, amount, version);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
