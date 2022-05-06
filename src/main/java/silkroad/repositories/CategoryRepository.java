package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import silkroad.entities.Category;

import java.util.List;
import java.util.Set;


public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT c FROM Category c where c.name in ?1")
    Set<Category> findAllDistinct(List<String> categories);
}