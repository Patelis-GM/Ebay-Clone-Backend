package silkroad.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import silkroad.utilities.TimeManager;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Entity
@Table(name = "auction")
@NoArgsConstructor
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Auction.findAllByCriteria", query = "SELECT DISTINCT a FROM Auction a JOIN FETCH a.address JOIN FETCH a.images WHERE a.id in :ids"),
        @NamedQuery(name = "Auction.findUserAuctionsByCriteria", query = "SELECT DISTINCT a FROM Auction a JOIN FETCH a.address JOIN FETCH a.images JOIN FETCH a.categories LEFT JOIN FETCH a.latestBid WHERE a.id in :ids"),
        @NamedQuery(name = "Auction.findUserPurchasesByCriteria", query = "SELECT DISTINCT a FROM Auction a  JOIN FETCH a.images JOIN FETCH a.latestBid WHERE a.id in :ids")
})
public class Auction {


    public Auction(String name, String description, Date endDate, Double buyPrice, Double firstBid, User seller) {
        this.name = name;
        this.description = description;
        this.startDate = TimeManager.now();
        this.endDate = endDate;
        this.buyPrice = buyPrice;
        this.firstBid = firstBid;
        this.highestBid = firstBid;
        this.seller = seller;
        this.latestBid = null;
        this.totalBids = 0L;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Lob
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "buy_price")
    private Double buyPrice;

    @Column(name = "first_bid", nullable = false)
    private Double firstBid;

    @Column(name = "total_bids", nullable = false)
    private Long totalBids;

    @Column(name = "highest_bid", nullable = false)
    private Double highestBid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @OneToMany(mappedBy = "auction")
    List<Bid> bids = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", nullable = true)
    private Bid latestBid;

    @ManyToMany
    @JoinTable(
            name = "classification",
            joinColumns = @JoinColumn(name = "auction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> categories = new HashSet<>();


    @OneToMany(mappedBy = "auction")
    private Set<Image> images = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "latitude", referencedColumnName = "latitude", nullable = false),
            @JoinColumn(name = "longitude", referencedColumnName = "longitude", nullable = false)
    })
    private Address address;


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static Predicate isActive(Root<Auction> root, CriteriaBuilder criteriaBuilder) {

        Predicate isNotDue = criteriaBuilder.greaterThan(root.get("endDate"), TimeManager.now());

        Predicate isBuyPricePresent = criteriaBuilder.isNotNull(root.get("buyPrice"));
        Predicate isHighestBidLessThanBuyPrice = criteriaBuilder.lessThan(root.get("highestBid"), root.get("buyPrice"));

        Predicate isBuyPriceAbsent = criteriaBuilder.isNull(root.get("buyPrice"));

        return criteriaBuilder.and(isNotDue, criteriaBuilder.or(isBuyPriceAbsent, criteriaBuilder.and(isBuyPricePresent, isHighestBidLessThanBuyPrice)));
    }

    public static Predicate wasSold(Root<Auction> root, CriteriaBuilder criteriaBuilder) {

        Predicate isBuyPricePresent = criteriaBuilder.isNotNull(root.get("buyPrice"));
        Predicate isHighestBidHigherThanOrEqualToBuyPrice = criteriaBuilder.greaterThanOrEqualTo(root.get("highestBid"), root.get("buyPrice"));
        Predicate wasSoldWithBuyPrice = criteriaBuilder.and(isBuyPricePresent, isHighestBidHigherThanOrEqualToBuyPrice);

        Predicate isDue = criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), TimeManager.now());
        Predicate bidsPresent = criteriaBuilder.greaterThanOrEqualTo(root.get("totalBids"), 1);
        Predicate wasSold = criteriaBuilder.and(isDue, bidsPresent);

        return criteriaBuilder.or(wasSoldWithBuyPrice, wasSold);
    }

    public static Predicate wasNotSold(Root<Auction> root, CriteriaBuilder criteriaBuilder) {

        Predicate isDue = criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), TimeManager.now());
        Predicate bidsAbsent = criteriaBuilder.equal(root.get("totalBids"), 0);

        return criteriaBuilder.and(isDue, bidsAbsent);
    }


}