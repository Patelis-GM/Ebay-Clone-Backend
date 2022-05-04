package silkroad.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "category")
@Getter
@Setter
@ToString
public class Category {

    @Id
    @Column(name = "id", nullable = false, length = 45)
    private String name;

    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
    Set<Auction> auctions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return name != null && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}