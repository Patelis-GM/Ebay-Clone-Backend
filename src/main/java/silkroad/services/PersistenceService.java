package silkroad.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Address;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Service
public class PersistenceService {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public Address persist(Address address) throws PersistenceException {
        entityManager.persist(address);
        entityManager.flush();
        return address;
    }
}
