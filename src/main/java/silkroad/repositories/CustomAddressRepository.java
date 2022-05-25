package silkroad.repositories;

import org.springframework.stereotype.Repository;
import silkroad.entities.Address;

import javax.persistence.PersistenceException;

@Repository
public interface CustomAddressRepository {

    Address persist(Address address) throws PersistenceException;
}
