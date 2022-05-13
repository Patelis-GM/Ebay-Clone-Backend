package silkroad.views.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.io.Serializable;

@Data
public class SellerJSON implements Serializable {

    @JsonProperty("UserID")
    private final String username;

    @JsonProperty("Rating")
    private final Double sellerRating;
}
