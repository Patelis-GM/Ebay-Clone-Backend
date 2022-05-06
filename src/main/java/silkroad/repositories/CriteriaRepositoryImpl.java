package silkroad.repositories;


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

        CriteriaQuery<ID> idQuery = getIDQuery(entityClass, idClass, idPath, criteriaBuilder, specification);
        List<ID> ids = entityManager.createQuery(idQuery).setFirstResult((int) pageRequest.getOffset()).setMaxResults(pageRequest.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = getCountQuery(entityClass, criteriaBuilder, specification);
        Long count = entityManager.createQuery(countQuery).getSingleResult();


        TypedQuery<T> typedQuery = entityManager.createNamedQuery(qlString, entityClass);
        typedQuery.setParameter("ids", ids);
        List<T> items = typedQuery.getResultList();

        return new PageImpl<>(items, pageRequest, count);
    }


    private CriteriaQuery<ID> getIDQuery(Class<T> entityClass, Class<ID> idClass, String idPath, CriteriaBuilder criteriaBuilder, Specification<T> specification) {
        CriteriaQuery<ID> idQuery = criteriaBuilder.createQuery(idClass);
        Root<T> root = idQuery.from(entityClass);
        idQuery.select(root.get(idPath));
        idQuery.where(specification.toPredicate(root, idQuery, criteriaBuilder));
        return idQuery;
    }

    private CriteriaQuery<Long> getCountQuery(Class<T> entityClass, CriteriaBuilder criteriaBuilder, Specification<T> specification) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> rootCount = countQuery.from(entityClass);
        countQuery.select(criteriaBuilder.count(rootCount));
        countQuery.where(specification.toPredicate(rootCount, countQuery, criteriaBuilder));
        return countQuery;
    }

}
