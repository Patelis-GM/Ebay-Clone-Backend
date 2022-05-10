package silkroad.views.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryXML implements Serializable {

    @JacksonXmlText
    private final String name;
}
