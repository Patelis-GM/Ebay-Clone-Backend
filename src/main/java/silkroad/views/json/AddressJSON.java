package silkroad.views.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressJSON implements Serializable {

    @JsonProperty("Latitude")
    private final Double latitude;

    @JsonProperty("Longitude")
    private final Double longitude;

    @JsonProperty("Location")
    private final String location;

    @JsonProperty("Country")
    private final String country;
}
