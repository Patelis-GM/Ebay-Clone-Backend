package silkroad.views.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BidderJSON implements Serializable {

    @JsonProperty("UserID")
    private final String username;

    @JsonProperty("Rating")
    private final Double buyerRating;

    @JsonProperty("Country")
    private final String country;

    @JsonProperty("Location")
    private final String location;
}
