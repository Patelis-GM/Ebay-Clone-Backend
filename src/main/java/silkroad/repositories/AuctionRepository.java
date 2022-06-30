package silkroad.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Auction;


import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, CustomAuctionRepository {


    /* The following query uses LockModeType.PESSIMISTIC_WRITE to avoid the following scenarios :
     *  (1) Bidder - Y bids on Auction - X
     *  (2) Owner - Z of X attempts to update / delete X
     *  (1) - (2) happen at the exact same time
     *
     *  (3) User - 1 bids on Auction - W
     *  (4) User - 2 bids on Auction - W
     *  (1) - (2) happen at the exact same time */
    @Transactional
    @Query("SELECT a FROM Auction a WHERE a.id = ?1 AND ?2 < a.endDate AND a.version = ?3 AND ((a.buyPrice is NULL) OR (a.buyPrice IS NOT NULL AND a.highestBid < a.buyPrice))")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findBiddableById(Long auctionID, Date now, Long version);


    /* The following query uses LockModeType.PESSIMISTIC_WRITE to avoid the following scenario :
    *  (1) Bidder - Y bids on Auction - X
    *  (2) Owner - Z of X attempts to update / delete X
    *  (1) - (2) happen at the exact same time */
    @Transactional
    @Query("SELECT a FROM Auction a WHERE a.id = ?1 AND a.latestBid is NULL AND a.endDate > ?2")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findUpdatableById(Long id, Date now);

    @Modifying
    @Transactional
    @Query("DELETE FROM Auction a WHERE a.id = ?1 AND a.latestBid is NULL AND a.endDate > ?2")
    Integer removeById(Long auctionID, Date now);

    @Query("SELECT a.seller.username FROM Auction a WHERE a.id = ?1")
    String findAuctionSellerById(Long auctionID);

    @Query("SELECT a FROM Auction a JOIN FETCH a.address JOIN FETCH a.images JOIN FETCH a.seller WHERE a.id = ?1")
    Optional<Auction> fetchAuctionDetails(Long auctionID);

    @Query("SELECT a FROM Auction a JOIN FETCH a.categories WHERE a = ?1")
    Auction fetchAuctionCategories(Auction auction);

    @Query("SELECT a.id FROM Auction a WHERE a.endDate > ?1 AND ((a.buyPrice is NULL) OR (a.buyPrice IS NOT NULL AND a.highestBid < a.buyPrice))")
    List<Long> findNotExpiredIds(Date now, Pageable pageable);

    @Query("SELECT DISTINCT a FROM Auction a JOIN FETCH a.address JOIN FETCH a.images WHERE a.endDate > ?2 AND ((a.buyPrice is NULL) OR (a.buyPrice IS NOT NULL AND a.highestBid < a.buyPrice)) AND a.id IN ?1")
    List<Auction> findRecommendationsByIds(List<Long> ids, Date now);

}
