package silkroad.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import silkroad.utilities.TimeManager;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "auction")
@NoArgsConstructor
@Getter
@Setter
public class Auction {


    public Auction(String name, String description, Date endDate, Double buyPrice, Double firstBid, User seller) {
        this.name = name;
        this.description = description;
        this.startDate = TimeManager.now();
        this.endDate = endDate;
        this.buyPrice = buyPrice;
        this.firstBid = firstBid;
        this.highestBid = 0.0;
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

    @OneToMany(mappedBy = "auction")
    private List<Image> images = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id")
    private Bid latestBid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "latitude", referencedColumnName = "latitude", nullable = false),
            @JoinColumn(name = "longitude", referencedColumnName = "longitude", nullable = false)
    })
    private Address address;

    @Version
    @Column(name = "version")
    private Long version;

    @ManyToMany
    @JoinTable(
            name = "classification",
            joinColumns = @JoinColumn(name = "auction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> categories = new HashSet<>();


    @OneToMany(mappedBy = "auction")
    private Set<SearchHistory> searchHistory = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Auction auction = (Auction) o;
        return id != null && Objects.equals(id, auction.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}