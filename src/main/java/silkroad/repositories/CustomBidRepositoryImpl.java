package silkroad.repositories;

import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Bid;
import silkroad.entities.Bid_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Transactional(readOnly = true)
public class CustomBidRepositoryImpl implements CustomBidRepository {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Page<Bid> findByUserId(Specification<Bid> specification, PageRequest pageRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> idQuery = getIDQuery(criteriaBuilder, specification);
        List<Long> ids = entityManager.createQuery(idQuery).setFirstResult((int) pageRequest.getOffset()).setMaxResults(pageRequest.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = getCountQuery(criteriaBuilder, specification);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        System.out.println(count);

        TypedQuery<Bid> typedQuery = entityManager.createQuery("SELECT DISTINCT b FROM Bid b JOIN FETCH b.auction as auction JOIN FETCH auction.images WHERE b.id in :ids", Bid.class);
        typedQuery.setParameter("ids", ids);
        typedQuery.setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false);
        List<Bid> bids = typedQuery.getResultList();

        return new PageImpl<>(bids, pageRequest, count);
    }

    private CriteriaQuery<Long> getIDQuery(CriteriaBuilder criteriaBuilder, Specification<Bid> specification) {
        CriteriaQuery<Long> idQuery = criteriaBuilder.createQuery(Long.class);
        Root<Bid> root = idQuery.from(Bid.class);
        idQuery.select(root.get(Bid_.ID));
        idQuery.where(specification.toPredicate(root, idQuery, criteriaBuilder));
        return idQuery;
    }

    private CriteriaQuery<Long> getCountQuery(CriteriaBuilder criteriaBuilder, Specification<Bid> specification) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Bid> rootCount = countQuery.from(Bid.class);
        countQuery.select(criteriaBuilder.count(rootCount));
        countQuery.where(specification.toPredicate(rootCount, countQuery, criteriaBuilder));
        return countQuery;
    }

}
