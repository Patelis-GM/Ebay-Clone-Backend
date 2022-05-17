package silkroad.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Auction;
import silkroad.entities.Bid;
import silkroad.entities.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b from Bid b WHERE b.auction.id = ?1 and b.bidder.username = ?2")
    Optional<Bid> findByAuctionAndBidder(Long auctionID, String username);

    @Modifying
    @Transactional
    @Query("UPDATE Auction a SET a.latestBid = ?2, a.highestBid = ?3, a.totalBids =?4 WHERE a.id = ?1")
    Integer bid(Long auctionID, Bid bid, Double amount, Long totalBids);


    @Query(value = "SELECT b FROM Bid b JOIN FETCH b.bidder WHERE b.auction.id = ?1",
            countQuery = "SELECT COUNT(b) FROM Bid b WHERE b.auction.id = ?1")
    Page<Bid> findByAuctionId(Long auctionID, Pageable pageable);


    @Query(value = "SELECT b FROM Bid b JOIN FETCH b.auction as auction JOIN FETCH auction.images WHERE b.bidder.username = ?1",
            countQuery = "SELECT COUNT(b) FROM Bid b WHERE b.bidder.username = ?1")
    Page<Bid> findByUserId(String username, Pageable pageRequest);
}