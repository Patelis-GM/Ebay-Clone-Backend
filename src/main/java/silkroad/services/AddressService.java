package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import silkroad.entities.Address;
import silkroad.entities.AddressID;
import silkroad.repositories.AddressRepository;
import silkroad.repositories.GeneralPurposeRepository;

import javax.persistence.PersistenceException;


@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final GeneralPurposeRepository<Address, AddressID> generalPurposeRepository;

    public Address createOrFindAddress(Address address) {
        try {
            return this.generalPurposeRepository.persist(address);
        } catch (PersistenceException e) {
            return this.addressRepository.getById(address.getCoordinates());
        }
    }

}
