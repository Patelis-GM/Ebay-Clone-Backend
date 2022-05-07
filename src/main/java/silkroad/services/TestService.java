package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import silkroad.dtos.auction.AuctionMapper;
import silkroad.dtos.auction.request.AuctionPosting;
import silkroad.entities.*;
import silkroad.exceptions.AuctionException;
import silkroad.exceptions.BidException;
import silkroad.repositories.*;
import silkroad.utilities.TimeManager;

import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class TestService {


    private final AddressRepository addressRepository;
    private final GeneralPurposeRepository<Address, AddressID> generalPurposeRepository;
    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final BidRepository bidRepository;

    public Address createOrFindAddress(Address address) {
        try {
            return this.generalPurposeRepository.persist(address);
        } catch (PersistenceException e) {
            return this.addressRepository.getById(address.getCoordinates());
        }
    }


    @Transactional
    public void createAuction(String username, AuctionPosting auctionDTO, MultipartFile[] multipartFiles) {

        Set<Category> auctionCategories = this.categoryRepository.findAllDistinct(auctionDTO.getCategories());

        if (auctionCategories.size() != auctionDTO.getCategories().size())
            throw new AuctionException(auctionDTO.getCategories().toString(), AuctionException.INVALID_CATEGORIES, HttpStatus.BAD_REQUEST);
        
        User seller = new User(username);

        Auction auction = new Auction(auctionDTO.getName(), auctionDTO.getDescription(), auctionDTO.getEndDate(), auctionDTO.getBuyPrice(), auctionDTO.getFirstBid(), seller);

        auction.setCategories(auctionCategories);

        auction.setAddress(auctionDTO.getAddress());

        this.auctionRepository.save(auction);

        this.imageService.uploadImages(auction, multipartFiles);
    }

    @Transactional
    public void updateAuction(String username, Long auctionID, AuctionPosting auctionDTO, MultipartFile[] multipartFiles) {
        
        Optional<Auction> optionalAuction = this.auctionRepository.findUpdatableById(auctionID);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.HAS_BID, HttpStatus.BAD_REQUEST);

        Auction auction = optionalAuction.get();

        Set<Category> auctionCategories = this.categoryRepository.findAllDistinct(auctionDTO.getCategories());

        auction.setAddress(auctionDTO.getAddress());
        auction.setName(auctionDTO.getName());
        auction.setDescription(auctionDTO.getDescription());
        auction.setEndDate(auctionDTO.getEndDate());
        auction.setBuyPrice(auctionDTO.getBuyPrice());
        auction.setFirstBid(auctionDTO.getFirstBid());
        auction.setCategories(auctionCategories);
    }

    @Transactional
    public void deleteAuction(String username, Long auctionID) {

    }

    @Transactional
    public void bid(String username, Long auctionID, Double amount, Long version) {

        Date bidDate = TimeManager.now();

        Optional<Auction> optionalAuction = this.auctionRepository.findBiddableById(auctionID, bidDate, version);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.MODIFIED_OR_EXPIRED, HttpStatus.BAD_REQUEST);

        Auction auction = optionalAuction.get();

        Double auctionHighestBid = auction.getHighestBid();
        Long totalBids = auction.getTotalBids();


        if (amount <= auctionHighestBid)
            throw new BidException(BidException.HIGHER_BID_EXISTS, HttpStatus.BAD_REQUEST);

        User bidder = this.userRepository.getById(username);

        Optional<Bid> optionalBid = this.bidRepository.findByAuctionAndBidder(auctionID, username);
        Bid bid;


        if (optionalBid.isPresent()) {
            bid = optionalBid.get();
            bid.setSubmissionDate(bidDate);
            bid.setAmount(amount);
        } else {
            bid = new Bid();
            bid.setBidder(bidder);
            bid.setAmount(amount);
            bid.setAuction(auction);
            bid.setSubmissionDate(bidDate);
            totalBids += 1;
            bid = this.bidRepository.save(bid);
        }

        this.bidRepository.bid(auctionID, bid, amount, totalBids);
    }
}
