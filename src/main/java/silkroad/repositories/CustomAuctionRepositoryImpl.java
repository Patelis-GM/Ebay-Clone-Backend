package silkroad.repositories;

import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Auction;
import silkroad.entities.Auction_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Transactional(readOnly = true)
public class CustomAuctionRepositoryImpl implements CustomAuctionRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<Auction> getUserAuctions(Specification<Auction> specification, PageRequest pageRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> idQuery = getIDQuery(criteriaBuilder, specification);
        List<Long> ids = entityManager.createQuery(idQuery).setFirstResult((int) pageRequest.getOffset()).setMaxResults(pageRequest.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = getCountQuery(criteriaBuilder, specification);
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        TypedQuery<Auction> typedQuery = entityManager.createQuery("SELECT DISTINCT a FROM Auction a JOIN FETCH a.categories JOIN FETCH a.address LEFT JOIN FETCH a.latestBid WHERE a.id in :ids", Auction.class);
        typedQuery.setParameter("ids", ids);
        typedQuery.setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false);
        List<Auction> auctions = typedQuery.getResultList();


        typedQuery = entityManager.createQuery("SELECT DISTINCT a FROM Auction a JOIN FETCH a.images WHERE a IN :auctions", Auction.class);
        typedQuery.setParameter("auctions", auctions);
        typedQuery.setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false);
        auctions = typedQuery.getResultList();

        return new PageImpl<>(auctions, pageRequest, count);
    }

    @Override
    public Page<Auction> browseAuctions(Specification<Auction> specification, PageRequest pageRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> idQuery = getIDQuery(criteriaBuilder, specification);
        List<Long> ids = entityManager.createQuery(idQuery).setFirstResult((int) pageRequest.getOffset()).setMaxResults(pageRequest.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = getCountQuery(criteriaBuilder, specification);
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        TypedQuery<Auction> typedQuery = entityManager.createQuery("SELECT DISTINCT a FROM Auction a JOIN FETCH a.address JOIN FETCH a.images WHERE a.id in :ids", Auction.class);
        typedQuery.setParameter("ids", ids);
        typedQuery.setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false);
        List<Auction> auctions = typedQuery.getResultList();

        return new PageImpl<>(auctions, pageRequest, count);
    }

    @Override
    public Page<Auction> getUserPurchases(Specification<Auction> specification, PageRequest pageRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> idQuery = getIDQuery(criteriaBuilder, specification);
        List<Long> ids = entityManager.createQuery(idQuery).setFirstResult((int) pageRequest.getOffset()).setMaxResults(pageRequest.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = getCountQuery(criteriaBuilder, specification);
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        TypedQuery<Auction> typedQuery = entityManager.createQuery("SELECT DISTINCT a FROM Auction a JOIN FETCH a.images JOIN FETCH a.latestBid WHERE a.id in :ids", Auction.class);
        typedQuery.setParameter("ids", ids);
        typedQuery.setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false);
        List<Auction> auctions = typedQuery.getResultList();

        return new PageImpl<>(auctions, pageRequest, count);
    }


    public List<Auction> exportAuctions(Specification<Auction> specification, Integer maxResults) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> idQuery = getIDQuery(criteriaBuilder, specification);
        List<Long> ids = entityManager.createQuery(idQuery).setMaxResults(maxResults).getResultList();


        TypedQuery<Auction> typedQuery = entityManager.createQuery("SELECT DISTINCT a FROM Auction a JOIN FETCH a.categories JOIN FETCH a.address JOIN FETCH a.seller WHERE a.id IN :ids", Auction.class).
                setParameter("ids", ids).
                setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false);

        List<Auction> auctions = typedQuery.getResultList();


        typedQuery = entityManager.createQuery("SELECT DISTINCT a FROM Auction a LEFT JOIN FETCH a.bids AS bids LEFT JOIN FETCH bids.bidder AS bidder LEFT JOIN FETCH bidder.address WHERE a IN :auctions ORDER BY a.id ASC", Auction.class);
        typedQuery.setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false);
        typedQuery.setParameter("auctions", auctions);
        auctions = typedQuery.getResultList();

        return auctions;
    }

    private CriteriaQuery<Long> getIDQuery(CriteriaBuilder criteriaBuilder, Specification<Auction> specification) {
        CriteriaQuery<Long> idQuery = criteriaBuilder.createQuery(Long.class);
        Root<Auction> root = idQuery.from(Auction.class);
        idQuery.select(root.get(Auction_.ID));
        idQuery.where(specification.toPredicate(root, idQuery, criteriaBuilder));
        return idQuery;
    }

    private CriteriaQuery<Long> getCountQuery(CriteriaBuilder criteriaBuilder, Specification<Auction> specification) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Auction> rootCount = countQuery.from(Auction.class);
        countQuery.select(criteriaBuilder.count(rootCount));
        countQuery.where(specification.toPredicate(rootCount, countQuery, criteriaBuilder));
        return countQuery;
    }

}
