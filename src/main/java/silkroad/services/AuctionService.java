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

        if (!this.auctionRepository.findAuctionSeller(auctionID).equals(authentication.getName()))
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

        if (!this.auctionRepository.findAuctionSeller(auctionID).equals(authentication.getName()))
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

    public PageResponse<AuctionBrowsingBasicDetails> getAuctions(Integer page, Integer size, String textSearch, Double minimumPrice, Double maximumPrice, String category, String location, Boolean hasBuyPrice) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Auction> auctionSpecification = (root, query, criteriaBuilder) -> {

            List<Predicate> auctionPredicates = new ArrayList<>();

            if (minimumPrice != null)
                auctionPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("highestBid"), minimumPrice));

            if (maximumPrice != null)
                auctionPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("highestBid"), maximumPrice));

            if (category != null) {
                SetJoin<Auction, Category> categorySetJoin = root.joinSet("categories", JoinType.INNER);
                categorySetJoin.on(criteriaBuilder.equal(categorySetJoin.get("name"), category));
            }

            if (location != null) {
                Join<Auction, Address> addressJoin = root.join("address");
//                addressJoin.on(criteriaBuilder.equal(addressJoin.get("country"), location));
                auctionPredicates.add(criteriaBuilder.equal(addressJoin.get("country"), location));
            }

            if (textSearch != null) {
                Predicate nameSearch = criteriaBuilder.like(root.get("name"), textSearch + "%");
                Predicate descriptionSearch = criteriaBuilder.like(root.get("description"), textSearch + "%");
                auctionPredicates.add(criteriaBuilder.or(nameSearch, descriptionSearch));
            }

            if (hasBuyPrice != null) {
                if (hasBuyPrice)
                    auctionPredicates.add(criteriaBuilder.isNotNull(root.get("buyPrice")));
                else
                    auctionPredicates.add(criteriaBuilder.isNull(root.get("buyPrice")));
            }

            auctionPredicates.add(Auction.isActive(root, criteriaBuilder));

            return criteriaBuilder.and(auctionPredicates.toArray(new Predicate[0]));

        };

        Page<Auction> auctionPage = this.auctionRepository.findByCriteria(Auction.class, Long.class, "id", pageRequest, auctionSpecification, "Auction.findAllByCriteria");

        List<AuctionBrowsingBasicDetails> auctionBrowsingBasicDetailsList = this.auctionMapper.mapToAuctionBrowsingBasicDetailsList(auctionPage.getContent());

        return new PageResponse<>(auctionBrowsingBasicDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());

    }

    public PageResponse<AuctionCompleteDetails> getUserAuctions(Authentication authentication, String userResource, Integer page, Integer size, Boolean sold, Boolean active) {

        String username = authentication.getName();

        if (!userResource.equals(username))
            throw new AuctionException(AuctionException.SELLER_BAD_CREDENTIALS, HttpStatus.FORBIDDEN);

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Auction> auctionSpecification = (root, query, criteriaBuilder) -> {

            List<Predicate> auctionPredicates = new ArrayList<>();

            auctionPredicates.add(criteriaBuilder.equal(root.get("seller").get("username"), username));

            if (sold != null) {

                if (sold)
                    auctionPredicates.add(Auction.wasSold(root, criteriaBuilder));
                else
                    auctionPredicates.add(Auction.wasNotSold(root, criteriaBuilder));
            } else if (active != null) {

                if (active)
                    auctionPredicates.add(Auction.isActive(root, criteriaBuilder));

            }


            return criteriaBuilder.and(auctionPredicates.toArray(new Predicate[0]));

        };


        Page<Auction> auctionPage = this.auctionRepository.findByCriteria(Auction.class, Long.class, "id", pageRequest, auctionSpecification, "Auction.findUserAuctionsByCriteria");

        List<AuctionCompleteDetails> auctionBasicDetailsList = this.auctionMapper.mapToAuctionCompleteDetailsDetailsList(auctionPage.getContent());

        return new PageResponse<>(auctionBasicDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());
    }

    public PageResponse<AuctionPurchaseDetails> getUserPurchases(Authentication authentication, String userResource, Integer page, Integer size, String sortField, String sortDirection) {

        String username = authentication.getName();

        if (!userResource.equals(username))
            throw new AuctionException(AuctionException.SELLER_BAD_CREDENTIALS, HttpStatus.FORBIDDEN);

        PageRequest pageRequest = PageRequest.of(page, size);


        Specification<Auction> auctionSpecification = (root, query, criteriaBuilder) -> {

            List<Predicate> auctionPredicates = new ArrayList<>();

            Join<Auction, Bid> auctionBidJoin = root.join("latestBid");
            auctionBidJoin.on(criteriaBuilder.and(criteriaBuilder.equal(auctionBidJoin.get("amount"), root.get("highestBid")), criteriaBuilder.equal(auctionBidJoin.get("bidder").get("username"), username)));
//            auctionPredicates.add(criteriaBuilder.equal(root.get("latestBid").get("bidder").get("username"), username));
            auctionPredicates.add(Auction.wasSold(root, criteriaBuilder));

            return criteriaBuilder.and(auctionPredicates.toArray(new Predicate[0]));

        };

        Page<Auction> auctionPage = this.auctionRepository.findByCriteria(Auction.class, Long.class, "id", pageRequest, auctionSpecification, "Auction.findUserPurchasesByCriteria");

        List<AuctionPurchaseDetails> auctionPurchaseDetailsList = this.auctionMapper.mapToAuctionPurchaseDetailsList(auctionPage.getContent());

        return new PageResponse<>(auctionPurchaseDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());

    }
}
