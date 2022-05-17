package silkroad.specifications;


import org.springframework.data.jpa.domain.Specification;
import silkroad.entities.Bid;
import silkroad.entities.Bid_;
import silkroad.entities.User_;


public class BidSpecificationBuilder {


    public static Specification<Bid> getUserBidsSpecification(String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Bid_.BIDDER).get(User_.USERNAME), username);
    }


}
