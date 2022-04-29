package silkroad.entities;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLInsert;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address")
@ToString
@Getter
@Setter
//@SQLInsert(sql = "INSERT IGNORE \n" + "        into\n" + "            address\n" + "            (country, location, street_name, street_number, zip_code, latitude, longitude) \n" +
//        "        values\n" +
//        "            (?, ?, ?, ?, ?, ?, ?)")
public class Address {

    @EmbeddedId
    @JsonAlias("coordinates")
    private AddressID addressID;

    @Column(name = "country", nullable = false, length = 45)
    private String country;

    @Column(name = "location", nullable = false, length = 45)
    private String location;

    @Column(name = "street_name", nullable = false, length = 45)
    private String streetName;

    @Column(name = "street_number", nullable = false, length = 45)
    private String streetNumber;

    @Column(name = "zip_code", nullable = false, length = 45)
    private String zipCode;

}