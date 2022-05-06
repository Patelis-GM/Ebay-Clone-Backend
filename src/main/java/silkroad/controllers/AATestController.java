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
import silkroad.services.AddressService;
import silkroad.services.AuctionService;
import silkroad.services.BidService;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class AATestController {

    private final AddressService addressService;
    private final AuctionService auctionService;
    private final BidService bidService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createAuctionTest(@RequestBody AuctionPosting auctionDTO) {
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
                return "Doris";
            }
        };
        this.auctionService.createAuction(authentication1, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<Void> updateAuctionTest(@RequestParam Long auctionID, @RequestBody AuctionPosting auctionDTO) {
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
                return "Doris";
            }
        };
        this.auctionService.updateAuction(authentication1, auctionID, auctionDTO, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteAuctionTest(@RequestParam(name = "id") Long auctionID) {
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
                return "Doris";
            }
        };
        this.auctionService.deleteAuction(authentication1, auctionID);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/bid", method = RequestMethod.POST)
    public ResponseEntity<Void> bidTest(@RequestParam(name = "id") Long auctionID, @RequestParam(name = "amount") Double amount, @RequestParam(name = "version") Long version) {
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
                return "Doris";
            }
        };
        this.bidService.bid(authentication1, auctionID, amount,version);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
