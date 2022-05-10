package silkroad.views.xml;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AuctionXML implements Serializable {

    @JacksonXmlProperty(isAttribute = true, localName = "ItemID")
    private final Long id;

    @JacksonXmlProperty(localName = "Name")
    private final String name;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Category")
    private final List<CategoryXML> categories;

    @JacksonXmlProperty(localName = "Currently")
    private final Double highestBid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(localName = "Buy_Price")
    private final Double buyPrice;

    @JacksonXmlProperty(localName = "First_Bid")
    private final Double firstBid;

    @JacksonXmlProperty(localName = "Number_of_Bids")
    private final Long totalBids;

    @JacksonXmlElementWrapper(localName = "Bids")
    @JacksonXmlProperty(localName = "Bid")
    private final List<BidXML> bids;

    @JacksonXmlProperty(localName = "Location")
    private final AddressXML address;

    @JacksonXmlProperty(localName = "Country")
    private final String country;

    @JacksonXmlProperty(localName = "Started")
    @JsonFormat(pattern = "MMM-dd-yy HH:mm:ss")
    private final Date startDate;

    @JacksonXmlProperty(localName = "Ends")
    @JsonFormat(pattern = "MMM-dd-yy HH:mm:ss")
    private final Date endDate;

    @JacksonXmlProperty(localName = "Seller")
    private final SellerXML seller;

    @JacksonXmlProperty(localName = "Description")
    private final String description;
}
