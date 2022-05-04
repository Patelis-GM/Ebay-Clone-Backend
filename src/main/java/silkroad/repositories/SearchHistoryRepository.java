package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import silkroad.entities.SearchHistory;
import silkroad.entities.SearchHistoryID;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, SearchHistoryID> {
}