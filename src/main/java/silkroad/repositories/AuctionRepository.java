package silkroad.repositories;

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

    @Transactional
    @Query("SELECT a FROM Auction a WHERE " +
            "a.id = ?1 AND " +
            "?2 < a.endDate AND " +
            "a.version = ?3 AND " +
            "((a.buyPrice is NULL) OR (a.buyPrice IS NOT NULL AND a.highestBid < a.buyPrice))")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findBiddableById(Long auctionID, Date now, Long version);

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

    @Query("SELECT a.id FROM Auction a WHERE a.endDate > ?1 AND " +
            "((a.buyPrice is NULL) OR (a.buyPrice IS NOT NULL AND a.highestBid < a.buyPrice))" +
            "ORDER BY a.id asc")
    List<Long> findSortedIds(Date now);

    @Query("SELECT DISTINCT a FROM Auction a JOIN FETCH a.address JOIN FETCH a.images WHERE a.id IN ?1")
    List<Auction> findRecommendationsByIds(List<Long> ids);


}
