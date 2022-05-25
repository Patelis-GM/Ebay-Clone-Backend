package silkroad.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import silkroad.entities.Bid;

@Repository
public interface CustomBidRepository {

    Page<Bid> findByUserId(Specification<Bid> specification, PageRequest pageRequest);

}
