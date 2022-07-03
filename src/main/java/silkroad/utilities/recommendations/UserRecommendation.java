package silkroad.utilities.recommendations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class UserRecommendation implements Comparable<UserRecommendation> {

    private Long auctionID;
    private Double auctionValue;


    @Override
    public int compareTo(UserRecommendation userRecommendation) {
        return Double.compare(this.auctionValue, userRecommendation.auctionValue);
    }
}
