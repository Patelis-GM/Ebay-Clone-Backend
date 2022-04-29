package silkroad.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@ToString
@Embeddable
@Getter
@Setter
public class AddressID implements Serializable {

    private static final long serialVersionUID = -8000043710256575195L;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AddressID entity = (AddressID) o;
        return Objects.equals(this.latitude, entity.latitude) &&
                Objects.equals(this.longitude, entity.longitude);
    }
}