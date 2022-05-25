package silkroad.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import silkroad.entities.Auction;

import java.util.List;

@Repository
public interface CustomAuctionRepository {

    Page<Auction> getUserPostedAuctions(Specification<Auction> specification, PageRequest pageRequest);
    Page<Auction> getUserPurchases(Specification<Auction> specification, PageRequest pageRequest);
    Page<Auction> browseAuctions(Specification<Auction> specification, PageRequest pageRequest);
    List<Auction> exportAuctions(Specification<Auction> specification, Integer maxResults);

}
