package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import silkroad.dtos.user.request.UserSignUpDTO;
import silkroad.entities.Address;
import silkroad.services.AddressService;
import silkroad.services.UserService;

@RestController
@AllArgsConstructor
public class SignUpController {

    private final UserService userService;
    private final AddressService addressService;

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity<Void> signUp(@RequestBody UserSignUpDTO user) {
        Address address = this.addressService.createOrFindAddress(user.getAddress());
        user.setAddress(address);
        this.userService.signUpUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
