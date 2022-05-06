package silkroad.dtos.bid.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BidPosting implements Serializable {

    private final Long version;
    private final Double amount;
}
