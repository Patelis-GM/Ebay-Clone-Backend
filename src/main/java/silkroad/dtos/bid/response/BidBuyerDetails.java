package silkroad.dtos.bid.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BidBuyerDetails implements Serializable {

    private final Long auctionID;
    private final String auctionName;
    private final Double amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date submissionDate;
}
