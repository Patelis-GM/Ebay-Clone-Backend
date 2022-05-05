package silkroad.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository
public class GeneralPurposeRepository<T, ID> {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public T persist(T entity) throws PersistenceException {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }
}
