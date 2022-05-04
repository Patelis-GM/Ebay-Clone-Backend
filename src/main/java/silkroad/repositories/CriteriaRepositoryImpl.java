package silkroad.repositories;

import org.hibernate.jpa.QueryHints;
import org.springframework.core.codec.Hints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CriteriaRepositoryImpl<T, ID> implements CriteriaRepository<T, ID> {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    @Transactional(readOnly = true)
    public Page<T> findByCriteria(Class<T> entityClass, Class<ID> idClass, String idPath, PageRequest pageRequest, Specification<T> specification, final String qlString) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<ID> idQuery = criteriaBuilder.createQuery(idClass);
        Root<T> root = idQuery.from(entityClass);
        List<ID> ids = entityManager.createQuery(idQuery.select(root.get(idPath)).where(specification.toPredicate(root, idQuery, entityManager.getCriteriaBuilder()))).setFirstResult((int) pageRequest.getOffset()).setMaxResults(pageRequest.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> rootCount = countQuery.from(entityClass);
        countQuery.select(criteriaBuilder.count(rootCount));
        countQuery.where(specification.toPredicate(root, idQuery, entityManager.getCriteriaBuilder()));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        TypedQuery<T> typedQuery = entityManager.createNamedQuery(qlString, entityClass);
        typedQuery.setParameter("ids", ids);
        List<T> items = typedQuery.getResultList();

        return new PageImpl<>(items, pageRequest, count);
    }
}
