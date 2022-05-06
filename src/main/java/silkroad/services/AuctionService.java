package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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
import silkroad.entities.*;
import silkroad.exceptions.AuctionException;
import silkroad.repositories.*;
import silkroad.specifications.AuctionSpecificationBuilder;

import javax.persistence.criteria.*;
import java.util.*;

@Service
@AllArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AuctionMapper auctionMapper;
    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public void createAuction(Authentication authentication, AuctionPosting auctionDTO, MultipartFile[] multipartFiles) {

        Set<Category> auctionCategories = this.categoryRepository.findAllDistinct(auctionDTO.getCategories());

        if (auctionCategories.size() != auctionDTO.getCategories().size())
            throw new AuctionException(auctionDTO.getCategories().toString(), AuctionException.INVALID_CATEGORIES, HttpStatus.BAD_REQUEST);

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

        if (!this.auctionRepository.existsById(auctionID))
            throw new AuctionException(AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        if (!this.auctionRepository.findAuctionSellerById(auctionID).equals(authentication.getName()))
            throw new AuctionException(auctionID.toString(), AuctionException.SELLER_BAD_CREDENTIALS, HttpStatus.FORBIDDEN);

        Optional<Auction> optionalAuction = this.auctionRepository.findUpdatableById(auctionID);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.HAS_BID, HttpStatus.BAD_REQUEST);

        Auction auction = optionalAuction.get();

        Set<Category> auctionCategories = this.categoryRepository.findAllDistinct(auctionDTO.getCategories());

        System.out.println(auctionCategories);

        if (auctionCategories.size() != auctionDTO.getCategories().size())
            throw new AuctionException(auctionDTO.getCategories().toString(), AuctionException.INVALID_CATEGORIES, HttpStatus.BAD_REQUEST);

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
            throw new AuctionException(AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        if (!this.auctionRepository.findAuctionSellerById(auctionID).equals(authentication.getName()))
            throw new AuctionException(auctionID.toString(), AuctionException.SELLER_BAD_CREDENTIALS, HttpStatus.FORBIDDEN);

        this.imageService.deleteImages(auctionID, true);

        if (this.auctionRepository.removeById(auctionID) == 0)
            throw new AuctionException(auctionID.toString(), AuctionException.HAS_BID, HttpStatus.BAD_REQUEST);
        else
            this.imageService.deleteImages(auctionID, false);
    }


    public AuctionBrowsingCompleteDetails getAuction(Authentication authentication, Long auctionID) {

        Optional<Auction> optionalAuction = this.auctionRepository.fetchAuctionWithCompleteDetails(auctionID);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        if (authentication != null) {
            SearchHistory searchHistoryRecord = new SearchHistory(new SearchHistoryID(auctionID, authentication.getName()), optionalAuction.get(), this.userRepository.getById(authentication.getName()));
            this.searchHistoryRepository.save(searchHistoryRecord);
        }

        return this.auctionMapper.mapToAuctionBrowsingCompleteDetails(optionalAuction.get());
    }


    public PageResponse<AuctionBrowsingBasicDetails> browseAuctions(Integer page, Integer size, String textSearch, Double minimumPrice, Double maximumPrice, String category, String location, Boolean hasBuyPrice) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Auction> auctionSpecification = AuctionSpecificationBuilder.getAuctionsBrowsingSpecification(textSearch, minimumPrice, maximumPrice, category, location, hasBuyPrice);

        Page<Auction> auctionPage = this.auctionRepository.findByCriteria(Auction.class, Long.class, Auction_.ID, pageRequest, auctionSpecification, "Auction.findAllByCriteria");

        List<AuctionBrowsingBasicDetails> auctionBrowsingBasicDetailsList = this.auctionMapper.mapToAuctionBrowsingBasicDetailsList(auctionPage.getContent());

        return new PageResponse<>(auctionBrowsingBasicDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());

    }

    public PageResponse<AuctionCompleteDetails> getUserPostedAuctions(Authentication authentication, String userResource, Integer page, Integer size, Boolean active, Boolean sold) {

        String username = authentication.getName();

        if (!userResource.equals(username))
            throw new AuctionException(AuctionException.SELLER_BAD_CREDENTIALS, HttpStatus.FORBIDDEN);

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Auction> auctionSpecification = AuctionSpecificationBuilder.getUserPostedAuctionsSpecification(username, active, sold);

        Page<Auction> auctionPage = this.auctionRepository.findByCriteria(Auction.class, Long.class, Auction_.ID, pageRequest, auctionSpecification, "Auction.findUserAuctionsByCriteria");

        List<AuctionCompleteDetails> auctionBasicDetailsList = this.auctionMapper.mapToAuctionCompleteDetailsDetailsList(auctionPage.getContent());

        return new PageResponse<>(auctionBasicDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());
    }

    public PageResponse<AuctionPurchaseDetails> getUserPurchasedAuctions(Authentication authentication, String userResource, Integer page, Integer size) {

        String username = authentication.getName();

        if (!userResource.equals(username))
            throw new AuctionException(AuctionException.BUYER_BAD_CREDENTIALS, HttpStatus.FORBIDDEN);

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Auction> auctionSpecification = AuctionSpecificationBuilder.getUserPurchasedAuctionsSpecification(username);

        Page<Auction> auctionPage = this.auctionRepository.findByCriteria(Auction.class, Long.class, Auction_.ID, pageRequest, auctionSpecification, "Auction.findUserPurchasesByCriteria");

        List<AuctionPurchaseDetails> auctionPurchaseDetailsList = this.auctionMapper.mapToAuctionPurchaseDetailsList(auctionPage.getContent());

        return new PageResponse<>(auctionPurchaseDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());

    }
}
