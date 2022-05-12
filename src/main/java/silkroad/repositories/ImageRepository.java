package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Image i WHERE i.auction.id = ?1")
    Integer deleteByAuction(Long auctionID);



}
