package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import silkroad.services.BidService;

@RestController
@AllArgsConstructor
public class BidController {

    private final BidService bidService;


}
