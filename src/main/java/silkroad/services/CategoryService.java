package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import silkroad.entities.Category;
import silkroad.entities.Category_;
import silkroad.repositories.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<String> getCategories() {
        return this.categoryRepository.findAll(Sort.by(Category_.NAME)).stream().map(Category::toString).collect(Collectors.toList());
    }
}
