package silkroad.dtos.auction.request;

import lombok.Data;
import silkroad.entities.Address;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class AuctionPosting implements Serializable {

    private String name;
    private String description;
    private Date endDate;
    private Double buyPrice;
    private Double firstBid;
    private List<String> categories;
    private Address address;


}
