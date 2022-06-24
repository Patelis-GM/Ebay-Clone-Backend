package silkroad.repositories;

import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Transactional(readOnly = true)
public class CustomBidRepositoryImpl implements CustomBidRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<Bid> findByUserId(Specification<Bid> specification, PageRequest pageRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> idQuery = getIDQuery(criteriaBuilder, specification, pageRequest);
        List<Long> ids = entityManager.createQuery(idQuery).setFirstResult((int) pageRequest.getOffset()).setMaxResults(pageRequest.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = getCountQuery(criteriaBuilder, specification);
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Bid> criteriaQuery = criteriaBuilder.createQuery(Bid.class);
        Root<Bid> bidRoot = criteriaQuery.from(Bid.class);
        bidRoot.fetch(Bid_.AUCTION, JoinType.INNER).fetch(Auction_.IMAGES);
        criteriaQuery.select(bidRoot);
        criteriaQuery.distinct(true);
        criteriaQuery.where(bidRoot.get(Auction_.ID).in(ids));
        criteriaQuery.orderBy(QueryUtils.toOrders(pageRequest.getSort(), bidRoot, criteriaBuilder));
        List<Bid> bids = entityManager.createQuery(criteriaQuery).getResultList();


        return new PageImpl<>(bids, pageRequest, count);
    }

    private CriteriaQuery<Long> getIDQuery(CriteriaBuilder criteriaBuilder, Specification<Bid> specification, @Nullable PageRequest pageRequest) {
        CriteriaQuery<Long> idQuery = criteriaBuilder.createQuery(Long.class);
        Root<Bid> root = idQuery.from(Bid.class);
        idQuery.select(root.get(Bid_.ID));
        idQuery.where(specification.toPredicate(root, idQuery, criteriaBuilder));
        if (pageRequest != null)
            idQuery.orderBy(QueryUtils.toOrders(pageRequest.getSort(), root, criteriaBuilder));
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
