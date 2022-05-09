package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silkroad.dtos.bid.BidMapper;
import silkroad.dtos.bid.response.BidBuyerDetails;
import silkroad.dtos.bid.response.BidSellerDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.entities.*;
import silkroad.exceptions.AuctionException;
import silkroad.exceptions.BidException;
import silkroad.repositories.AuctionRepository;
import silkroad.repositories.BidRepository;
import silkroad.repositories.SearchHistoryRepository;
import silkroad.repositories.UserRepository;
import silkroad.utilities.TimeManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BidService {


    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final BidMapper bidMapper;
    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public void bid(Authentication authentication, Long auctionID, Double amount, Long version) {

        if (!this.auctionRepository.existsById(auctionID))
            throw new AuctionException(AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        Date bidDate = TimeManager.now();

        Optional<Auction> optionalAuction = this.auctionRepository.findBiddableById(auctionID, bidDate, version);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.MODIFIED_OR_EXPIRED, HttpStatus.BAD_REQUEST);

        Auction auction = optionalAuction.get();

        if (auction.getSeller().getUsername().equals(authentication.getName()))
            throw new BidException(BidException.BIDDER_BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);

        Double auctionFirstBid = auction.getFirstBid();
        Double auctionHighestBid = auction.getHighestBid();
        Long totalBids = auction.getTotalBids();

        if (amount <= 0 || amount < auctionFirstBid)
            throw new BidException(BidException.INVALID_BID_AMOUNT, HttpStatus.BAD_REQUEST);

        if (amount <= auctionHighestBid)
            throw new BidException(BidException.HIGHER_BID_EXISTS, HttpStatus.BAD_REQUEST);

        User bidder = this.userRepository.getById(authentication.getName());

        Optional<Bid> optionalBid = this.bidRepository.findByAuctionAndBidder(auctionID, authentication.getName());
        Bid bid;

        SearchHistory searchHistoryRecord = new SearchHistory(new SearchHistoryID(auctionID, authentication.getName()), auction, bidder);
        this.searchHistoryRepository.save(searchHistoryRecord);

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


    public PageResponse<BidSellerDetails> getAuctionsBids(Authentication authentication, Long auctionID, Integer page, Integer size) {

        if (!this.auctionRepository.existsById(auctionID))
            throw new AuctionException(AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        if (!this.auctionRepository.findAuctionSellerById(auctionID).equals(authentication.getName()))
            throw new AuctionException(AuctionException.SELLER_BAD_CREDENTIALS, HttpStatus.FORBIDDEN);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Bid_.SUBMISSION_DATE).descending());

        Page<Bid> bidsPage = this.bidRepository.findByAuctionId(auctionID, pageRequest);

        List<BidSellerDetails> auctionBids = this.bidMapper.mapBidsToBidSellerDetailsList(bidsPage.getContent());

        return new PageResponse<>(auctionBids, bidsPage.getNumber() + 1, bidsPage.getTotalPages(), bidsPage.getTotalElements(), bidsPage.getNumberOfElements());
    }

    public PageResponse<BidBuyerDetails> getUserBids(Authentication authentication, Integer page, Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Bid_.SUBMISSION_DATE).descending());

        Page<Bid> bidsPage = this.bidRepository.findByUserId(authentication.getName(), pageRequest);

        List<BidBuyerDetails> userBids = this.bidMapper.mapBidsToBidBuyerDetailsList(bidsPage.getContent());

        return new PageResponse<>(userBids, bidsPage.getNumber() + 1, bidsPage.getTotalPages(), bidsPage.getTotalElements(), bidsPage.getNumberOfElements());
    }
}
