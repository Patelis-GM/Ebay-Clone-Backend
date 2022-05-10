package silkroad.views.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SellerXML implements Serializable {

    @JacksonXmlProperty(isAttribute = true, localName = "Rating")
    private final Double sellerRating;

    @JacksonXmlProperty(isAttribute = true, localName = "UserID")
    private final String username;
}
