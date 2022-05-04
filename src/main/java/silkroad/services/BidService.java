package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silkroad.dtos.bid.BidMapper;
import silkroad.dtos.bid.response.BidDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.entities.Auction;
import silkroad.entities.Bid;
import silkroad.entities.User;
import silkroad.exceptions.AuctionException;
import silkroad.exceptions.BidException;
import silkroad.repositories.AuctionRepository;
import silkroad.repositories.BidRepository;
import silkroad.repositories.UserRepository;
import silkroad.utilities.TimeManager;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BidService {


    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final BidMapper bidMapper;

    @Transactional
    public void bid(String username, Long auctionID, Double amount) {

        if (amount <= 0)
            throw new BidException(BidException.INVALID_AMOUNT, HttpStatus.BAD_REQUEST);

        Date bidDate = TimeManager.now();

        Optional<Auction> optionalAuction = this.auctionRepository.findNonExpiredByIdWithPessimisticLock(auctionID, bidDate);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        Auction auction = optionalAuction.get();

        if (auction.getSeller().getUsername().equals(username))
            throw new BidException(BidException.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);

        Double auctionFirstBid = auction.getFirstBid();
        Double auctionHighestBID = auction.getHighestBid();

        if (amount < auction.getFirstBid())
            throw new BidException(BidException.INVALID_AMOUNT, HttpStatus.BAD_REQUEST);

        if (!Objects.equals(auctionFirstBid, auctionHighestBID) && amount <= auctionHighestBID)
            throw new BidException(BidException.INVALID_AMOUNT, HttpStatus.BAD_REQUEST);

        User bidder = this.userRepository.getById(username);

        Long totalBids = auction.getTotalBids();
        if (!this.bidRepository.existsByBidderAndAuctionId(bidder, auctionID))
            totalBids += 1;

        if (auction.getBuyPrice() != null && amount >= auction.getBuyPrice()) {
            if (this.bidRepository.bid(auctionID, bidder, amount, totalBids) == 0)
                throw new BidException(BidException.ALREADY_BOUGHT, HttpStatus.BAD_REQUEST);
        } else if (this.bidRepository.bid(auctionID, bidder, amount, totalBids) == 0)
            throw new BidException(BidException.HIGHER_BID, HttpStatus.BAD_REQUEST);

        Optional<Bid> optionalBid = this.bidRepository.findByAuctionAndBidder(auctionID, username);

        if (optionalBid.isPresent())
            this.bidRepository.updateByAuctionAndBidder(amount, bidDate, auctionID, username);

        else {
            Bid bid = new Bid();
            bid.setBidder(bidder);
            bid.setAmount(amount);
            bid.setAuction(auction);
            bid.setSubmissionDate(bidDate);
            this.bidRepository.save(bid);
        }

    }

    public PageResponse<BidDetails> getBids(Long auctionID, Integer page, Integer size, String sortField, String sortDirection) {

        PageRequest pageRequest;

        if (sortDirection.equals("asc"))
            pageRequest = PageRequest.of(page, size, Sort.by(sortField).ascending());
        else
            pageRequest = PageRequest.of(page, size, Sort.by(sortField).descending());


        Page<Bid> bidsPage = this.bidRepository.findByAuctionId(auctionID, pageRequest);
        List<BidDetails> auctionBids = this.bidMapper.mapBidToBidsDetails(bidsPage.getContent());

        return new PageResponse<>(auctionBids, bidsPage.getNumber() + 1, bidsPage.getTotalPages(), bidsPage.getTotalElements(), bidsPage.getNumberOfElements());
    }
}
