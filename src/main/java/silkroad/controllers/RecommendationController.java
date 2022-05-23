package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import silkroad.dtos.auction.response.AuctionBrowsingDetails;
import silkroad.services.RecommendationService;

import java.util.List;

@RestController
@AllArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @RequestMapping(value = "/recommend", method = RequestMethod.GET)
    public ResponseEntity<List<AuctionBrowsingDetails>> recommend(Authentication authentication) {
        return new ResponseEntity<>(this.recommendationService.recommend(authentication), HttpStatus.OK);
    }

}
