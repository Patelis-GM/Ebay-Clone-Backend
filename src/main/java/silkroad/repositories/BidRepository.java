package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Auction;
import silkroad.entities.Bid;
import silkroad.entities.User;

import java.util.Date;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {


    Boolean existsByBidderAndAuctionId(User bidder, Long auctionID);

    @Query("SELECT b from Bid b WHERE b.auction.id = ?1 and b.bidder.username = ?2")
    Optional<Bid> findByAuctionAndBidder(Long auctionID, String username);

    @Query("UPDATE Bid b SET b.amount = ?1, b.submissionDate = ?2 WHERE b.auction.id = ?3 AND b.bidder.username = ?4")
    @Modifying
    @Transactional
    void updateByAuctionAndBidder(Double amount, Date submissionDate, Long auctionID, String username);

    @Modifying
    @Transactional
    @Query("UPDATE Auction a SET a.bidder = ?2, a.highestBid = ?3, a.totalBids =?4 WHERE " +
            "a.id = ?1 AND " +
            "a.highestBid < ?3 AND " +
            "((a.buyPrice is NULL) OR (a.buyPrice IS NOT NULL AND a.highestBid < a.buyPrice))")
    Integer bid(Long auctionID, User bidder, Double amount, Long totalBids);

}