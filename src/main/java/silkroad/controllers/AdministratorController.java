package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silkroad.dtos.page.PageResponse;
import silkroad.dtos.user.response.UserBasicDetails;
import silkroad.dtos.user.response.UserCompleteDetails;
import silkroad.services.UserService;

import javax.websocket.server.PathParam;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/administration")
public class AdministratorController {

    private final UserService userService;


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<UserBasicDetails>> getUsers(@RequestParam(name = "approved", required = false) Boolean approvalStatus, @RequestParam(name = "page") Integer pageIndex, @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.userService.getUsersBasicDetails(approvalStatus, pageIndex - 1, pageSize), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserCompleteDetails> getUser(@PathVariable String username) {
        return new ResponseEntity<>(this.userService.getUser(username), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.PUT)
    public ResponseEntity<Void> approveUser(@PathVariable String username) {
        this.userService.approveUser(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
