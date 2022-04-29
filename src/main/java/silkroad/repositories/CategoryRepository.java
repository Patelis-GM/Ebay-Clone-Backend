package silkroad.repositories;

import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Category;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT DISTINCT c FROM Category c where c.id in ?1")
    Set<Category> findAll(List<String> categories);
}