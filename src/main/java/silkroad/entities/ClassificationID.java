package silkroad.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class ClassificationID implements Serializable {

    private static final long serialVersionUID = -2714467866585788395L;

    @Column(name = "auction_id", nullable = false)
    private Long auctionId;

    @Column(name = "category_id", nullable = false, length = 45)
    private String categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClassificationID entity = (ClassificationID) o;
        return Objects.equals(this.auctionId, entity.auctionId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionId, categoryId);
    }

}