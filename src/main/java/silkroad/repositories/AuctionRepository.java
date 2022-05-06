package silkroad.repositories;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Auction;


import javax.persistence.LockModeType;
import java.util.Date;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, JpaSpecificationExecutor<Auction>, CriteriaRepository<Auction, Long> {

    @Transactional
    @Query("SELECT a FROM Auction a WHERE " +
            "a.id = ?1 AND " +
            "?2 < a.endDate AND " +
            "a.version = ?3 AND " +
            "((a.buyPrice is NULL) OR (a.buyPrice IS NOT NULL AND a.highestBid < a.buyPrice))")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findBiddableById(Long auctionID, Date now, Long version);

    @Transactional
    @Query("SELECT a FROM Auction a WHERE a.id = ?1 and a.latestBid is NULL")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findUpdatableById(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Auction a WHERE a.id = ?1 AND a.latestBid is NULL")
    Integer removeById(Long auctionID);

    @Query("SELECT a.seller.username FROM Auction a WHERE a.id = ?1")
    String findAuctionSellerById(Long auctionID);

    @Query("SELECT a FROM Auction a JOIN FETCH a.address JOIN FETCH a.images JOIN FETCH a.seller WHERE a.id = ?1")
    Optional<Auction> fetchAuctionWithCompleteDetails(Long auctionID);


}
