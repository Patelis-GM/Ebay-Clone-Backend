package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import silkroad.dtos.bid.response.BidDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.services.BidService;

@RestController
@AllArgsConstructor
public class BidController {

    private final BidService bidService;


}
