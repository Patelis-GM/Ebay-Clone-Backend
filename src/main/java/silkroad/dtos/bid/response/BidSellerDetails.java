package silkroad.dtos.bid.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BidSellerDetails implements Serializable {

    private final Long id;
    private final String bidder;
    private final Double bidderRating;
    private final Double amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date submissionDate;
}
