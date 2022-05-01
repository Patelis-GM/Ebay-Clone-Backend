package silkroad.repositories;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Auction;
import silkroad.entities.User;


import javax.persistence.LockModeType;
import java.util.Date;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, JpaSpecificationExecutor<Auction> {

    @Transactional
    @Query("SELECT a FROM Auction a WHERE " +
            "a.id = ?1 AND " +
            "?2 < a.endDate AND " +
            "((a.buyPrice is NULL) OR (a.buyPrice IS NOT NULL AND a.highestBid < a.buyPrice))")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findNonExpiredByIdWithPessimisticLock(Long auctionID, Date now);


    @Transactional
    @Query("SELECT a FROM Auction a WHERE a.id = ?1 and a.bidder is NULL")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findByIdWithPessimisticLock(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Auction a WHERE " +
            "a.id = ?1 AND " +
            "a.bidder is NULL")
    Integer removeById(Long auctionID);

    @Query("SELECT a FROM Auction a JOIN FETCH a.address JOIN FETCH a.images JOIN FETCH a.seller WHERE a.id = ?1")
    Optional<Auction> fetchAuctionWithCompleteDetails(Long auctionID);
}
