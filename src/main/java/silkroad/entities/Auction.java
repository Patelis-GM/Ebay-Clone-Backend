package silkroad.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import silkroad.utilities.TimeManager;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "auction")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Auction {

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

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "highest_bid", nullable = false)
    private Double highestBid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    @ToString.Exclude
    private User seller;

    @OneToMany(mappedBy = "auction")
    @ToString.Exclude
    List<Bid> bids = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id")
    @ToString.Exclude
    private User bidder;

    @ManyToMany
    @JoinTable(
            name = "classification",
            joinColumns = @JoinColumn(name = "auction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @ToString.Exclude
    Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "auction")
    @ToString.Exclude
    private List<Image> images = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "latitude", referencedColumnName = "latitude", nullable = false),
            @JoinColumn(name = "longitude", referencedColumnName = "longitude", nullable = false)
    })
    @ToString.Exclude
    private Address address;

    @ManyToMany(mappedBy = "searchHistory")
    private Set<User> users;

    public Auction(String name, String description, Date endDate, Double buyPrice, Double firstBid, User seller) {
        this.name = name;
        this.description = description;
        this.startDate = TimeManager.now();
        this.endDate = endDate;
        this.buyPrice = buyPrice;
        this.firstBid = firstBid;
        this.seller = seller;
        this.highestBid = firstBid;
        this.bidder = null;
        this.version = 0L;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @org.springframework.data.annotation.Transient
    public Boolean getExpired() {
        return TimeManager.now().getTime() >= this.endDate.getTime() || (this.buyPrice != null && this.highestBid >= buyPrice && this.bidder != null);
    }



}