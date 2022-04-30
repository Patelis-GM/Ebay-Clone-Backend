package silkroad.dtos.page;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {


    private final List<T> objects;
    private final Integer pageIndex;
    private final Integer totalPages;
    private final Integer totalElements;
    private final Integer totalElementsInPage;

    public PageResponse(Page<T> page) {
        this.objects = page.getContent();
        this.pageIndex = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.totalElements = this.objects.size();
        this.totalElementsInPage = page.getNumberOfElements();
    }

}
