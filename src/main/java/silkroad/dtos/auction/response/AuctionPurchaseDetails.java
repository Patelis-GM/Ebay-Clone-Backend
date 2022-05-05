package silkroad.dtos.auction.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class AuctionPurchaseDetails implements Serializable {

    private final Long id;
    private final String name;
    private final String seller;
    private final Double cost;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date date;
    private final List<String> images;
}
