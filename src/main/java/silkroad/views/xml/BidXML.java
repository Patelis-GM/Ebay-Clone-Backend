package silkroad.views.xml;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BidXML implements Serializable {

    @JacksonXmlProperty(localName = "Bidder")
    private final BidderXML bidder;

    @JacksonXmlProperty(localName = "Time")
    @JsonFormat(pattern = "MMM-dd-yy HH:mm:ss")
    private final Date submissionDate;

    @JacksonXmlProperty(localName = "Amount")
    private final Double amount;

}
