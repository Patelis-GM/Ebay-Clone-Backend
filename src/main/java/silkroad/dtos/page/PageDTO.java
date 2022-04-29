package silkroad.dtos.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;


public class PageDTO<T> {

    private List<T> objects;
    private Integer index;
    private Integer pages;
    private Integer count;

    public PageDTO(Page<T> page) {
        this.objects = page.getContent();
        this.index = page.getNumber();
    }
}
