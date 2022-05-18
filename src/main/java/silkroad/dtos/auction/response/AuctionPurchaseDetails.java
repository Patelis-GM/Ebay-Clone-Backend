package silkroad.dtos.auction.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class AuctionPurchaseDetails implements Serializable {

    private final Long id;
    private final String name;
    private final String seller;
    private final Double cost;
    private final Date date;
    private final List<String> images;
}
