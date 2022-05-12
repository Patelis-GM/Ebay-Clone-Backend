package silkroad.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import silkroad.dtos.page.PageResponse;
import silkroad.dtos.user.response.UserBasicDetails;
import silkroad.dtos.user.response.UserCompleteDetails;
import silkroad.entities.Auction;
import silkroad.services.AuctionService;
import silkroad.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/administration")
public class AdministratorController {

    private final UserService userService;
    private final AuctionService auctionService;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<UserBasicDetails>> getUsers(@RequestParam(name = "approved", required = false) Boolean approvalStatus, @RequestParam(name = "page") Integer pageIndex, @RequestParam(name = "size") Integer pageSize) {
        return new ResponseEntity<>(this.userService.getUsersBasicDetails(approvalStatus, pageIndex - 1, pageSize), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserCompleteDetails> getUser(@PathVariable String username) {
        return new ResponseEntity<>(this.userService.getUser(username), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/users/{username}", method = RequestMethod.PUT)
    public ResponseEntity<Void> approveUser(@PathVariable String username) {
        this.userService.approveUser(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/auctions/export", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> exportAuctions(@RequestParam(name = "format") String format,
                                                              @RequestParam(name = "from") Long from,
                                                              @RequestParam(name = "to") Long to) throws IOException {

        String xmlString = this.auctionService.exportAuctions(format, from, to);
//        byte[] xmlStringByteArray = xmlString.getBytes();
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Transfer-Encoding", "binary");
//        response.setContentType(APPLICATION_XML_VALUE);
//        response.setContentLength(xmlString.length());
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Auctions.xml");


        InputStream stringInputStream = new ByteArrayInputStream(xmlString.getBytes());
        InputStreamResource inputStreamResource = new InputStreamResource(stringInputStream);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        httpHeaders.setContentDispositionFormData("attachment", "ExportAuctions.xml");

        //this copies the content of your string to the output stream
//        IOUtils.copy(stringInputStream, response.getOutputStream());
//
//
//        response.flushBuffer();

        return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
//        return new ResponseEntity<>(this.auctionService.exportAuctions(format, from, to), HttpStatus.OK);
    }


}
