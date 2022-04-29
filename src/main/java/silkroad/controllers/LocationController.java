package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import silkroad.services.AddressService;
import java.util.List;

@RestController
@AllArgsConstructor
public class LocationController {

    private final AddressService addressService;

//    @RequestMapping(value = "/locations", method = RequestMethod.GET)
//    public ResponseEntity<List<Statistics>> getLocations(@RequestParam Integer top) {
//        return new ResponseEntity<>(this.addressService.getLocations(top), HttpStatus.OK);
//    }

}
