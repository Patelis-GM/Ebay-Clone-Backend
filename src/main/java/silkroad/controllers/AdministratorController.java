package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silkroad.dtos.user.request.UserSignUpDTO;
import silkroad.services.UserService;

import java.time.Instant;

@AllArgsConstructor
@RestController
public class AdministratorController {

    private final UserService userService;

    @GetMapping(value = "/admin")
    public String hiAdmin(@RequestParam Long haha) throws InterruptedException {
        Instant instant = Instant.now();
        System.out.println("Came in at :" + instant.toEpochMilli());
        return "Ok";
    }

    @GetMapping(value = "/approved")
    public String hiApproved() {
        return "Hi, approved User";
    }



}
