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

    public Address createOrFindAddress(Address address) {
        try {
            return this.addressRepository.persist(address);
        } catch (PersistenceException e) {
            return this.addressRepository.getById(address.getCoordinates());
        }
    }

}
