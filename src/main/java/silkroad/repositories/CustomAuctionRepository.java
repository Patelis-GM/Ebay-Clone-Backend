package silkroad.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import silkroad.entities.Auction;

import java.util.Date;
import java.util.List;

public interface CustomAuctionRepository {

    Page<Auction> getUserAuctions(Specification<Auction> specification, PageRequest pageRequest);
    Page<Auction> getUserPurchases(Specification<Auction> specification, PageRequest pageRequest);
    Page<Auction> browseAuctions(Specification<Auction> specification, PageRequest pageRequest);
    List<Auction> exportAuctions(Date from, Date to, Integer maxResults);

}
