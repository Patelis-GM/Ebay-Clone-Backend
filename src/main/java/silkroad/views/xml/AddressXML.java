package silkroad.views.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressXML implements Serializable {

    @JacksonXmlProperty(isAttribute = true, localName = "Latitude")
    private final Double latitude;

    @JacksonXmlProperty(isAttribute = true, localName = "Longitude")
    private final Double longitude;

    @JacksonXmlText
    private final String location;
}
