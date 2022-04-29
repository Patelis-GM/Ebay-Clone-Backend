package silkroad.repositories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuctionSearchCriteria {
    private String key;
    private Object object;
    private AuctionSearchOperation auctionSearchOperation;
}
