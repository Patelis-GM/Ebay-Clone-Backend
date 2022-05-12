package silkroad.dtos.bid.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BidBuyerDetails implements Serializable {

    private final Long auctionID;

    private final String auctionName;

    private final Double amount;

    private final Date submissionDate;
}
