package silkroad.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public interface CriteriaRepository<T, ID> {


    Page<T> findByCriteria(Class<T> entityClass, Class<ID> idClass, String idPath, PageRequest pageRequest, Specification<T> specification, String qlString);

}
