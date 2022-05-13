package silkroad.specifications;

import org.springframework.data.jpa.domain.Specification;
import silkroad.entities.*;
import silkroad.utilities.TimeManager;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuctionSpecificationBuilder {


    public static Predicate isActive(Root<Auction> root, CriteriaBuilder criteriaBuilder) {

        Predicate isNotDue = AuctionSpecificationBuilder.isNotDue(root, criteriaBuilder);

        Predicate isBuyPriceAbsent = AuctionSpecificationBuilder.isBuyPriceAbsent(root, criteriaBuilder);

        Predicate isBuyPricePresent = AuctionSpecificationBuilder.isBuyPricePresent(root, criteriaBuilder);
        Predicate isHighestBidLessThanBuyPrice = criteriaBuilder.lessThan(root.get(Auction_.HIGHEST_BID), root.get(Auction_.BUY_PRICE));

        return criteriaBuilder.and(isNotDue, criteriaBuilder.or(isBuyPriceAbsent, criteriaBuilder.and(isBuyPricePresent, isHighestBidLessThanBuyPrice)));
    }

    public static Predicate isNotActive(Root<Auction> root, CriteriaBuilder criteriaBuilder) {

        Predicate isDue = AuctionSpecificationBuilder.isDue(root, criteriaBuilder);
        Predicate wasSold = AuctionSpecificationBuilder.wasSold(root, criteriaBuilder);

        return criteriaBuilder.or(isDue, wasSold);
    }

    public static Predicate wasSold(Root<Auction> root, CriteriaBuilder criteriaBuilder) {

        Predicate isBuyPricePresent = AuctionSpecificationBuilder.isBuyPricePresent(root, criteriaBuilder);
        Predicate isHighestBidHigherThanOrEqualToBuyPrice = criteriaBuilder.greaterThanOrEqualTo(root.get(Auction_.HIGHEST_BID), root.get(Auction_.BUY_PRICE));
        Predicate wasSoldWithBuyPrice = criteriaBuilder.and(isBuyPricePresent, isHighestBidHigherThanOrEqualToBuyPrice);

        Predicate isDue = AuctionSpecificationBuilder.isDue(root, criteriaBuilder);
        Predicate hasAtLeastOneBid = criteriaBuilder.greaterThanOrEqualTo(root.get(Auction_.TOTAL_BIDS), 1L);
        Predicate wasSold = criteriaBuilder.and(isDue, hasAtLeastOneBid);

        return criteriaBuilder.or(wasSoldWithBuyPrice, wasSold);
    }

    public static Predicate wasNotSold(Root<Auction> root, CriteriaBuilder criteriaBuilder) {

        Predicate isDue = AuctionSpecificationBuilder.isDue(root, criteriaBuilder);
        Predicate hasNoBids = criteriaBuilder.equal(root.get(Auction_.TOTAL_BIDS), 0);

        return criteriaBuilder.and(isDue, hasNoBids);
    }

    public static Predicate isBuyPricePresent(Root<Auction> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.isNotNull(root.get(Auction_.BUY_PRICE));
    }

    public static Predicate isBuyPriceAbsent(Root<Auction> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.isNull(root.get(Auction_.BUY_PRICE));
    }

    public static Predicate isDue(Root<Auction> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThanOrEqualTo(root.get(Auction_.END_DATE), TimeManager.now());
    }

    public static Predicate isNotDue(Root<Auction> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThan(root.get(Auction_.END_DATE), TimeManager.now());
    }

    public static Specification<Auction> getAuctionsBrowsingSpecification(String textSearch, Double minimumPrice, Double maximumPrice, String category, String location, Boolean hasBuyPrice) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> auctionPredicates = new ArrayList<>();

            if (minimumPrice != null)
                auctionPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Auction_.HIGHEST_BID), minimumPrice));

            if (maximumPrice != null)
                auctionPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Auction_.HIGHEST_BID), maximumPrice));

            if (category != null) {
                SetJoin<Auction, Category> categorySetJoin = root.joinSet(Auction_.CATEGORIES, JoinType.INNER);
                categorySetJoin.on(criteriaBuilder.equal(categorySetJoin.get(Category_.NAME), category));
            }

            if (location != null) {
                Join<Auction, Address> addressJoin = root.join(Auction_.ADDRESS, JoinType.INNER);
                addressJoin.on(criteriaBuilder.or(criteriaBuilder.like(addressJoin.get(Address_.COUNTRY), location + "%"), criteriaBuilder.like(addressJoin.get(Address_.LOCATION), location + "%")));
            }

            if (textSearch != null) {
                Predicate nameSearch = criteriaBuilder.like(root.get(Auction_.NAME), textSearch + "%");
                Predicate descriptionSearch = criteriaBuilder.like(root.get(Auction_.DESCRIPTION), textSearch + "%");
                auctionPredicates.add(criteriaBuilder.or(nameSearch, descriptionSearch));
            }

            if (hasBuyPrice != null) {

                if (hasBuyPrice)
                    auctionPredicates.add(AuctionSpecificationBuilder.isBuyPricePresent(root, criteriaBuilder));
                else
                    auctionPredicates.add(AuctionSpecificationBuilder.isBuyPriceAbsent(root, criteriaBuilder));
            }

            auctionPredicates.add(AuctionSpecificationBuilder.isActive(root, criteriaBuilder));

            return criteriaBuilder.and(auctionPredicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Auction> getUserPostedAuctionsSpecification(String username, Boolean active, Boolean sold) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> auctionPredicates = new ArrayList<>();

            if (active != null) {

                if (active)
                    auctionPredicates.add(AuctionSpecificationBuilder.isActive(root, criteriaBuilder));
                else
                    auctionPredicates.add(AuctionSpecificationBuilder.isNotActive(root, criteriaBuilder));

            } else if (sold != null) {

                if (sold)
                    auctionPredicates.add(AuctionSpecificationBuilder.wasSold(root, criteriaBuilder));
                else
                    auctionPredicates.add(AuctionSpecificationBuilder.wasNotSold(root, criteriaBuilder));
            }

            auctionPredicates.add(criteriaBuilder.equal(root.get(Auction_.SELLER).get(User_.USERNAME), username));

            return criteriaBuilder.and(auctionPredicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Auction> getUserPurchasedAuctionsSpecification(String username) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> auctionPredicates = new ArrayList<>();

            Join<Auction, Bid> auctionBidJoin = root.join(Auction_.LATEST_BID);

            auctionBidJoin.on(criteriaBuilder.and(criteriaBuilder.equal(auctionBidJoin.get(Bid_.AMOUNT), root.get(Auction_.HIGHEST_BID)),
                    criteriaBuilder.equal(auctionBidJoin.get(Bid_.BIDDER).get(User_.USERNAME), username)));

            auctionPredicates.add(AuctionSpecificationBuilder.wasSold(root, criteriaBuilder));

            return criteriaBuilder.and(auctionPredicates.toArray(new Predicate[0]));
        };

    }


    public static Specification<Auction> getExportAuctionsSpecification(Date from, Date to) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(Auction_.START_DATE), from, to);
    }

}
