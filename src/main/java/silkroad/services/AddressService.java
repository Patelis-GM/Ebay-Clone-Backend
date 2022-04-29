package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import silkroad.entities.Address;
import silkroad.repositories.AddressRepository;

import javax.persistence.PersistenceException;


@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final PersistenceService persistenceService;

    public Address createOrFindAddress(Address address) {
        try {
            Address newAddress = this.persistenceService.persist(address);
            return newAddress;
        } catch (PersistenceException e) {
            return this.addressRepository.getById(address.getAddressID());
        }
    }

}
