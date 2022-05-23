package silkroad.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silkroad.services.ImageService;


@RestController
@AllArgsConstructor
public class MediaController {

    private final ImageService imageService;


    @RequestMapping(value = "/media/{auctionID}/{fileName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(@PathVariable Long auctionID, @PathVariable String fileName) {
        return new ResponseEntity<>(this.imageService.getImage(auctionID, fileName), HttpStatus.OK);
    }

}
