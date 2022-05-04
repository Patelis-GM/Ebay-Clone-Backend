package silkroad.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "image")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Image {

    @Id
    @Column(name = "path", nullable = false, length = 256)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Image image = (Image) o;
        return path != null && Objects.equals(path, image.path);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}