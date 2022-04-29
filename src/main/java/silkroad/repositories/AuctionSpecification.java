package silkroad.repositories;

import org.springframework.data.jpa.domain.Specification;
import silkroad.entities.*;

import javax.persistence.FetchType;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionSpecification implements Specification<Auction> {

    private List<AuctionSearchCriteria> searchCriteria = new ArrayList<>();


    public void addSearchCriterion(AuctionSearchCriteria auctionSearchCriteria) {
        this.searchCriteria.add(auctionSearchCriteria);
    }

    @Override
    public Specification<Auction> and(Specification<Auction> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Auction> or(Specification<Auction> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Auction> auction, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        for (AuctionSearchCriteria auctionSearchCriteria : searchCriteria) {
            switch (auctionSearchCriteria.getAuctionSearchOperation()) {
                case GREATER_THAN_OR_EQUAL:
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(auction.get(auctionSearchCriteria.getKey()), auctionSearchCriteria.getObject().toString()));
                    break;
                case LESS_THAN_OR_EQUAL:
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(auction.get(auctionSearchCriteria.getKey()), auctionSearchCriteria.getObject().toString()));
                    break;
                case LOCATION:
                    Join<Auction, Address> addressJoin = auction.join("address", JoinType.INNER);
                    addressJoin.on(criteriaBuilder.equal(addressJoin.get("country"),"Italy"));
//                    predicates.add(criteriaBuilder.equal(addressJoin.get("country"), "Italy"));
                    break;
                case MEMBER_OF:
//                    SetJoin<Auction, Category> auctionCategorySetJoin1 = auction.joinSet("categories", JoinType.INNER);
//                    auctionCategorySetJoin1.on(criteriaBuilder.equal(auctionCategorySetJoin1.get("categoryId"),"Clothing"))
//                    Category category = new Category();
//                    category.setID("Clothing");
//                    predicates.add(criteriaBuilder.isMember(category, auction.get("categories")));
//                    criteriaBuilder.equal(auctionCategorySetJoin1.)
                    SetJoin<Auction,Category> categorySetJoin = auction.joinSet("categories",JoinType.INNER);
                    categorySetJoin.on(criteriaBuilder.equal(categorySetJoin.get("ID"),"Clothing"));

                    break;
                case LIKE:
                    Predicate buyPriceNullPredicate = criteriaBuilder.isNull(auction.get("buyPrice"));
                    Predicate buyPriceNotNullPredicate = criteriaBuilder.isNotNull(auction.get("buyPrice"));
                    Predicate highestBidLessThanBuyPrice = criteriaBuilder.lessThan(auction.get("highestBid"),auction.get("buyPrice"));
                    Predicate hasBuyPrice = criteriaBuilder.and(buyPriceNotNullPredicate,highestBidLessThanBuyPrice);
                    predicates.add(criteriaBuilder.or(buyPriceNullPredicate, hasBuyPrice));
                    break;
            }
        }
//        query = query.distinct(true);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
