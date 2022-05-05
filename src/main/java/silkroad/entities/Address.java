package silkroad.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {

    @EmbeddedId
    private AddressID coordinates;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "street_name", nullable = false, length = 100)
    private String streetName;

    @Column(name = "street_number", nullable = false, length = 45)
    private String streetNumber;

    @Column(name = "zip_code", nullable = false, length = 45)
    private String zipCode;

}