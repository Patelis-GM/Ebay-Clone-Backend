package silkroad.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import silkroad.entities.Address;
import silkroad.entities.AddressID;

public interface AddressRepository extends JpaRepository<Address, AddressID>, CustomAddressRepository {

}