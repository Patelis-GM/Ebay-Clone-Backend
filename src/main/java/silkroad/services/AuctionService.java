package silkroad.services;

import com.ctc.wstx.api.WstxOutputProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import silkroad.dtos.auction.AuctionMapper;
import silkroad.dtos.auction.request.AuctionPosting;
import silkroad.dtos.auction.response.AuctionBrowsingBasicDetails;
import silkroad.dtos.auction.response.AuctionBrowsingCompleteDetails;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.dtos.auction.response.AuctionPurchaseDetails;
import silkroad.dtos.page.PageResponse;
import silkroad.dtos.user.request.Username;
import silkroad.entities.*;
import silkroad.exceptions.AuctionException;
import silkroad.exceptions.UserException;
import silkroad.repositories.*;
import silkroad.specifications.AuctionSpecificationBuilder;
import silkroad.utilities.TimeManager;
import silkroad.views.json.AuctionJSONCollection;
import silkroad.views.json.JSONMapper;
import silkroad.views.xml.AuctionXMLCollection;
import silkroad.views.xml.XMLMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service
@AllArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final SearchHistoryService searchHistoryService;
    private final AuctionMapper auctionMapper;
    private final XMLMapper xmlExportMapper;
    private final JSONMapper jsonExportMapper;


    @Transactional
    public void createAuction(Authentication authentication, AuctionPosting auctionDTO, MultipartFile[] multipartFiles) {

        if (multipartFiles.length == 0)
            throw new AuctionException(AuctionException.AUCTION_NO_MEDIA, HttpStatus.BAD_REQUEST);

        Set<Category> auctionCategories = this.categoryRepository.findAllDistinct(auctionDTO.getCategories());

        if (auctionCategories.size() != auctionDTO.getCategories().size())
            throw new AuctionException(auctionDTO.getCategories().toString(), AuctionException.AUCTION_INVALID_CATEGORIES, HttpStatus.BAD_REQUEST);

        String username = authentication.getName();
        User seller = new User(username);

        Auction auction = new Auction(auctionDTO.getName(), auctionDTO.getDescription(), auctionDTO.getEndDate(), auctionDTO.getBuyPrice(), auctionDTO.getFirstBid(), seller);

        auction.setCategories(auctionCategories);

        auction.setAddress(auctionDTO.getAddress());

        this.auctionRepository.save(auction);

        this.imageService.uploadImages(auction, multipartFiles);
    }

    @Transactional
    public void updateAuction(Authentication authentication, Long auctionID, AuctionPosting auctionDTO, MultipartFile[] multipartFiles) {

        if (multipartFiles.length == 0)
            throw new AuctionException(AuctionException.AUCTION_NO_MEDIA, HttpStatus.BAD_REQUEST);

        if (!this.auctionRepository.existsById(auctionID))
            throw new AuctionException(AuctionException.AUCTION_NOT_FOUND, HttpStatus.NOT_FOUND);

        if (!this.auctionRepository.findAuctionSellerById(auctionID).equals(authentication.getName()))
            throw new UserException(UserException.USER_ACTION_FORBIDDEN, HttpStatus.FORBIDDEN);

        Optional<Auction> optionalAuction = this.auctionRepository.findUpdatableById(auctionID, TimeManager.now());

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.AUCTION_HAS_BID_OR_EXPIRED, HttpStatus.BAD_REQUEST);

        Auction auction = optionalAuction.get();

        Set<Category> auctionCategories = this.categoryRepository.findAllDistinct(auctionDTO.getCategories());


        if (auctionCategories.size() != auctionDTO.getCategories().size())
            throw new AuctionException(auctionDTO.getCategories().toString(), AuctionException.AUCTION_INVALID_CATEGORIES, HttpStatus.BAD_REQUEST);

        auction.setAddress(auctionDTO.getAddress());
        auction.setName(auctionDTO.getName());
        auction.setDescription(auctionDTO.getDescription());
        auction.setEndDate(auctionDTO.getEndDate());
        auction.setBuyPrice(auctionDTO.getBuyPrice());
        auction.setFirstBid(auctionDTO.getFirstBid());
        auction.setCategories(auctionCategories);

        this.imageService.updateImages(auction, multipartFiles);
    }

    @Transactional
    public void deleteAuction(Authentication authentication, Long auctionID) {

        if (!this.auctionRepository.existsById(auctionID))
            throw new AuctionException(AuctionException.AUCTION_NOT_FOUND, HttpStatus.NOT_FOUND);

        if (!this.auctionRepository.findAuctionSellerById(auctionID).equals(authentication.getName()))
            throw new UserException(UserException.USER_ACTION_FORBIDDEN, HttpStatus.FORBIDDEN);

        this.imageService.deleteImages(auctionID, true);

        this.searchHistoryService.deleteByAuctionId(auctionID);

        if (this.auctionRepository.removeById(auctionID) == 0)
            throw new AuctionException(auctionID.toString(), AuctionException.AUCTION_HAS_BID_OR_EXPIRED, HttpStatus.BAD_REQUEST);

        this.imageService.deleteImages(auctionID, false);
    }


    public Username getAuctionSeller(Long auctionID) {

        if (!this.auctionRepository.existsById(auctionID))
            throw new AuctionException(AuctionException.AUCTION_NOT_FOUND, HttpStatus.NOT_FOUND);

        return new Username(this.auctionRepository.findAuctionSellerById(auctionID));
    }

    public AuctionBrowsingCompleteDetails getAuction(Authentication authentication, Long auctionID) {

        Optional<Auction> optionalAuction = this.auctionRepository.fetchAuctionWithCompleteDetails(auctionID);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.AUCTION_NOT_FOUND, HttpStatus.NOT_FOUND);

        Auction auction = optionalAuction.get();

        if (authentication != null)
            this.searchHistoryService.recordUserInteraction(this.userRepository.getById(authentication.getName()), auction);

        return this.auctionMapper.toAuctionBrowsingCompleteDetails(auction);
    }

    public PageResponse<AuctionBrowsingBasicDetails> browseAuctions(Integer page, Integer size, String textSearch, Double minimumPrice, Double maximumPrice, String category, String location, Boolean hasBuyPrice) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Auction> auctionSpecification = AuctionSpecificationBuilder.getAuctionsBrowsingSpecification(textSearch, minimumPrice, maximumPrice, category, location, hasBuyPrice);

        Page<Auction> auctionPage = this.auctionRepository.browseAuctions(auctionSpecification, pageRequest);

        List<AuctionBrowsingBasicDetails> auctionBrowsingBasicDetailsList = this.auctionMapper.toAuctionBrowsingBasicDetailsList(auctionPage.getContent());

        return new PageResponse<>(auctionBrowsingBasicDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());

    }

    public PageResponse<AuctionCompleteDetails> getUserPostedAuctions(Authentication authentication, Integer page, Integer size, Boolean active, Boolean sold) {

        String username = authentication.getName();

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Auction> auctionSpecification = AuctionSpecificationBuilder.getUserPostedAuctionsSpecification(username, active, sold);

        Page<Auction> auctionPage = this.auctionRepository.getUserAuctions(auctionSpecification, pageRequest);

        List<AuctionCompleteDetails> auctionBasicDetailsList = this.auctionMapper.toAuctionCompleteDetailsDetailsList(auctionPage.getContent());

        return new PageResponse<>(auctionBasicDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());
    }

    public PageResponse<AuctionPurchaseDetails> getUserPurchasedAuctions(Authentication authentication, Integer page, Integer size) {

        String username = authentication.getName();

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Auction> auctionSpecification = AuctionSpecificationBuilder.getUserPurchasedAuctionsSpecification(username);

        Page<Auction> auctionPage = this.auctionRepository.getUserPurchases(auctionSpecification, pageRequest);

        List<AuctionPurchaseDetails> auctionPurchaseDetailsList = this.auctionMapper.toAuctionPurchaseDetailsList(auctionPage.getContent());

        return new PageResponse<>(auctionPurchaseDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());

    }

    public ResponseEntity<InputStreamResource> exportAuctions(Boolean asJSON, Date from, Date to) throws JsonProcessingException {

        HttpHeaders httpHeaders = new HttpHeaders();
        InputStream stringInputStream;
        InputStreamResource inputStreamResource;
        Specification<Auction> auctionSpecification = AuctionSpecificationBuilder.getExportAuctionsSpecification(from, to);

        final Integer EXPORT_AUCTIONS_MAX_RESULTS = 500;
        List<Auction> auctions = this.auctionRepository.exportAuctions(auctionSpecification, EXPORT_AUCTIONS_MAX_RESULTS);

        if (asJSON) {
            ObjectMapper objectMapper = getJSONMapper();
            AuctionJSONCollection auctionJSONCollection = new AuctionJSONCollection(jsonExportMapper.toAuctionJSONList(auctions));
            String jsonString = objectMapper.writeValueAsString(auctionJSONCollection);
            stringInputStream = new ByteArrayInputStream(jsonString.getBytes());
            inputStreamResource = new InputStreamResource(stringInputStream);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setContentDispositionFormData("attachment", "ExportAuctions.json");
        } else {
            XmlMapper xmlMapper = getXMLMapper();
            AuctionXMLCollection auctionXMLCollection = new AuctionXMLCollection(xmlExportMapper.toAuctionXMLList(auctions));
            String xmlString = xmlMapper.writeValueAsString(auctionXMLCollection);
            stringInputStream = new ByteArrayInputStream(xmlString.getBytes());
            inputStreamResource = new InputStreamResource(stringInputStream);
            httpHeaders.setContentType(MediaType.APPLICATION_XML);
            httpHeaders.setContentDispositionFormData("attachment", "ExportAuctions.xml");
        }

        return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
    }

    private XmlMapper getXMLMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.getFactory().getXMLOutputFactory().setProperty(WstxOutputProperties.P_AUTOMATIC_END_ELEMENTS, false);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        return xmlMapper;
    }

    private ObjectMapper getJSONMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }


}
