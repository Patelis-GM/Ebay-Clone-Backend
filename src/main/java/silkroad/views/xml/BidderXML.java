package silkroad.views.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BidderXML implements Serializable {

    @JacksonXmlProperty(isAttribute = true, localName = "Rating")
    private final Double buyerRating;

    @JacksonXmlProperty(isAttribute = true, localName = "UserID")
    private final String username;

    @JacksonXmlProperty(localName = "Location")
    private final String location;

    @JacksonXmlProperty(localName = "Country")
    private final String country;
}
