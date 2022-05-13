package silkroad.views.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BidJSON implements Serializable {

    @JsonProperty("Bidder")
    private final BidderJSON bidder;

    @JsonProperty("Amount")
    private final String amount;

    @JsonProperty("Time")
    @JsonFormat(pattern = "MMM-dd-yy HH:mm:ss")
    private final Date submissionDate;
}
