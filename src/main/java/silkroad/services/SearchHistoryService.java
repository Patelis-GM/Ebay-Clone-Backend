package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Auction;
import silkroad.entities.SearchHistory;
import silkroad.entities.SearchHistoryID;
import silkroad.entities.User;
import silkroad.repositories.SearchHistoryRepository;

@Service
@AllArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public void recordUserInteraction(User user, Auction auction) {
        SearchHistoryID searchHistoryID = new SearchHistoryID(auction.getId(), user.getUsername());
        if (this.searchHistoryRepository.existsById(searchHistoryID))
            this.searchHistoryRepository.updateInteractionsById(searchHistoryID);
        else
            this.searchHistoryRepository.save(new SearchHistory(searchHistoryID, auction, user));
    }

    @Transactional
    public void deleteByAuctionId(Long auctionID) {
        this.searchHistoryRepository.deleteByAuctionId(auctionID);
    }
}
