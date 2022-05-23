package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.SearchHistory;
import silkroad.entities.SearchHistoryID;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, SearchHistoryID> {

    @Transactional
    @Modifying
    @Query("DELETE FROM SearchHistory s WHERE s.auction.id = ?1 ")
    Integer deleteByAuctionId(Long auctionID);

    @Transactional
    @Modifying
    @Query("UPDATE SearchHistory s SET s.interactions = s.interactions + 1 WHERE s.id = ?1 and s.interactions < ?2")
    Integer updateInteractionsById(SearchHistoryID searchHistoryID, Long interactionsCeiling);

    @Query("SELECT s FROM SearchHistory s WHERE s.id.auctionId IN ?1")
    List<SearchHistory> findAllByAuctionId(List<Long> sortedAuctions);
}