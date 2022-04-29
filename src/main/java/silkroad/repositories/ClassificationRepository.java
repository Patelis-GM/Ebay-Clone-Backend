package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Classification;
import silkroad.entities.ClassificationID;

public interface ClassificationRepository extends JpaRepository<Classification, ClassificationID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Classification c where c.auction.id = ?1")
    void deleteByAuction(Long auctionID);

}