package silkroad.dtos.bid.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BidDetails implements Serializable {
    private final Long id;
    private final String bidder;
    private final Double bidderRating;
    private final Double amount;
    private final Date submissionDate;
}
