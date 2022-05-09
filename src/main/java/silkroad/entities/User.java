package silkroad.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import silkroad.utilities.TimeManager;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "approved", nullable = false)
    private Boolean approved;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 45)
    private String phone;

    @Column(name = "tin", nullable = false, length = 45)
    private String tin;

    @Column(name = "buyer_rating", nullable = false)
    private Double buyerRating;

    @Column(name = "seller_rating", nullable = false)
    private Double sellerRating;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "latitude", referencedColumnName = "latitude", nullable = false),
            @JoinColumn(name = "longitude", referencedColumnName = "longitude", nullable = false)
    })
    private Address address;

    @Column(name = "join_date", nullable = false)
    private Date joinDate;

    @ManyToMany
    @JoinTable(
            name = "searchhistory",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "auction_id")})
    private Set<Auction> searchHistory = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "messageaccess",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id"))
    private Set<Message> accessedMessages = new LinkedHashSet<>();

    public void grantAccessToMessage(Message message) {
        this.accessedMessages.add(message);
    }

    public void removeAccessToMessage(Message message) {
        this.accessedMessages.remove(message);
    }

    @OneToMany(mappedBy = "sender")
    private Set<Message> sentMessages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "recipient")
    private Set<Message> receivedMessages = new LinkedHashSet<>();

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password, String email, String firstName, String lastName, String phone, String tin, Address address, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.tin = tin;
        this.address = address;
        this.role = role;
        this.approved = false;
        this.buyerRating = 0.0;
        this.sellerRating = 0.0;
        this.joinDate = TimeManager.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return username != null && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}