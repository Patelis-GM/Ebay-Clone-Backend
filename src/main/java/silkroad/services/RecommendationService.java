package silkroad.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import silkroad.dtos.auction.AuctionMapper;
import silkroad.dtos.auction.response.AuctionBrowsingDetails;
import silkroad.entities.Auction;
import silkroad.entities.Auction_;
import silkroad.entities.SearchHistory;
import silkroad.repositories.AuctionRepository;
import silkroad.repositories.SearchHistoryRepository;
import silkroad.repositories.UserRepository;
import silkroad.utilities.TimeManager;
import silkroad.utilities.recommendations.RecommendationMatrix;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RecommendationService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final RecommendationMatrix recommendationMatrix;
    private final AuctionMapper auctionMapper;


    public RecommendationService(UserRepository userRepository, AuctionRepository auctionRepository, SearchHistoryRepository searchHistoryRepository, AuctionMapper auctionMapper) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.searchHistoryRepository = searchHistoryRepository;
        this.recommendationMatrix = new RecommendationMatrix();
        this.auctionMapper = auctionMapper;
    }

    public List<AuctionBrowsingDetails> recommend(Authentication authentication) {
        final Integer maximumRecommendations = 5;
        List<Long> recommendations = this.recommendationMatrix.recommend(authentication, maximumRecommendations);
        List<Auction> auctions = this.auctionRepository.findRecommendationsByIds(recommendations);
        return this.auctionMapper.toAuctionBrowsingDetailsList(auctions);
    }

    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedDelay = 7200)
    public void factorize() {
        List<String> sortedUsers = this.userRepository.findSortedUsernames();
        final Integer maxAuctions = 150;
        PageRequest pageRequest = PageRequest.of(0, maxAuctions, Sort.by(Auction_.ID).ascending());
        List<Long> sortedAuctions = this.auctionRepository.findNotExpiredIds(TimeManager.now(),pageRequest);
        List<SearchHistory> searchHistoryRecords = this.searchHistoryRepository.findAllByAuctionId(sortedAuctions);
        this.recommendationMatrix.factorize(sortedUsers, sortedAuctions, searchHistoryRecords);
    }


}
