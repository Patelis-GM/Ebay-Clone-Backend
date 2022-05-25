package silkroad.repositories;

import org.springframework.transaction.annotation.Transactional;
import silkroad.entities.Address;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

public class CustomAddressRepositoryImpl implements CustomAddressRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public Address persist(Address address) throws PersistenceException {
        entityManager.persist(address);
        entityManager.flush();
        return address;
    }

}
