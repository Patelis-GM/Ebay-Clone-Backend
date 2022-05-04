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
import silkroad.dtos.auction.response.AuctionBasicDetails;
import silkroad.dtos.auction.response.AuctionCompleteDetails;
import silkroad.dtos.auction.response.AuctionDto;
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
    public void createAuction(String username, AuctionPosting auctionDTO, MultipartFile[] multipartFiles) {

        Set<Category> auctionCategories = this.categoryRepository.findAll(auctionDTO.getCategories());

        if (auctionCategories.size() != auctionDTO.getCategories().size())
            throw new AuctionException(auctionDTO.getCategories().toString(), AuctionException.INVALID_CATEGORIES, HttpStatus.BAD_REQUEST);

        User seller = new User(username);

        Auction auction = new Auction(auctionDTO.getName(), auctionDTO.getDescription(), auctionDTO.getEndDate(), auctionDTO.getBuyPrice(), auctionDTO.getFirstBid(), seller);

        auction.setCategories(auctionCategories);

        auction.setAddress(auctionDTO.getAddress());

        this.auctionRepository.save(auction);

        this.imageService.uploadImages(auction, multipartFiles);
    }

    @Transactional
    public void updateAuction(String username, Long auctionID, AuctionPosting auctionDTO, MultipartFile[] multipartFiles) {

        Set<Category> auctionCategories = this.categoryRepository.findAll(auctionDTO.getCategories());

        if (auctionCategories.size() != auctionDTO.getCategories().size())
            throw new AuctionException(auctionDTO.getCategories().toString(), AuctionException.INVALID_CATEGORIES, HttpStatus.BAD_REQUEST);

        Optional<Auction> optionalAuction = this.auctionRepository.findByIdWithPessimisticLock(auctionID);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        Auction auction = optionalAuction.get();

        if (!auction.getSeller().getUsername().equals(username))
            throw new AuctionException(auctionID.toString(), AuctionException.BAD_CREDENTIALS, HttpStatus.FORBIDDEN);


        auction.setAddress(auctionDTO.getAddress());
        auction.setName(auctionDTO.getName());
        auction.setDescription(auctionDTO.getDescription());
        auction.setEndDate(auctionDTO.getEndDate());
        auction.setBuyPrice(auctionDTO.getBuyPrice());
        auction.setFirstBid(auctionDTO.getFirstBid());
        auction.setCategories(auctionCategories);
        this.auctionRepository.save(auction);

        this.imageService.updateImages(auction, multipartFiles);
    }

    @Transactional
    public void deleteAuction(String username, Long auctionID) {

        Optional<Auction> optionalAuction = this.auctionRepository.findByIdWithPessimisticLock(auctionID);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        Auction auction = optionalAuction.get();

        if (!auction.getSeller().getUsername().equals(username))
            throw new AuctionException(auctionID.toString(), AuctionException.BAD_CREDENTIALS, HttpStatus.FORBIDDEN);

        this.imageService.deleteImages(auctionID, true);

        if (this.auctionRepository.removeById(auctionID) == 0)
            throw new AuctionException(auctionID.toString(), AuctionException.AT_LEAST_ONE_BID, HttpStatus.BAD_REQUEST);
        else
            this.imageService.deleteImages(auctionID, false);
    }

    public AuctionCompleteDetails getAuction(Authentication authentication, Long auctionID) {

        Optional<Auction> optionalAuction = this.auctionRepository.fetchAuctionWithCompleteDetails(auctionID);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        if (authentication != null) {
            SearchHistory searchHistoryRecord = new SearchHistory(new SearchHistoryID(auctionID, authentication.getName()), optionalAuction.get(), this.userRepository.getById(authentication.getName()));
            this.searchHistoryRepository.save(searchHistoryRecord);
        }

        return this.auctionMapper.mapToAuctionCompleteDetails(optionalAuction.get());
    }

    public PageResponse<AuctionBasicDetails> getAuctions(Integer page, Integer size, String textSearch, Double minimumPrice, Double maximumPrice, String category, String location, Boolean hasBuyPrice) {

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
                addressJoin.on(criteriaBuilder.equal(addressJoin.get("country"), location));
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

        List<AuctionBasicDetails> auctionBasicDetailsList = this.auctionMapper.mapToAuctionsBasicDetails(auctionPage.getContent());

        return new PageResponse<>(auctionBasicDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());

    }


    public PageResponse<AuctionDto> getUserAuctions(Authentication authentication, Integer page, Integer size, Boolean sold) {

        String username = authentication.getName();

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Auction> auctionSpecification = (root, query, criteriaBuilder) -> {

            List<Predicate> auctionPredicates = new ArrayList<>();

            auctionPredicates.add(criteriaBuilder.equal(root.get("seller").get("username"), username));

          if (sold != null) {

                if (sold)
                    auctionPredicates.add(Auction.isSold(root, criteriaBuilder));
                else
                    auctionPredicates.add(Auction.isExpired(root, criteriaBuilder));
            }

            else
                auctionPredicates.add(Auction.isActive(root, criteriaBuilder));

            return criteriaBuilder.and(auctionPredicates.toArray(new Predicate[0]));

        };


        Page<Auction> auctionPage = this.auctionRepository.findByCriteria(Auction.class, Long.class, "id", pageRequest, auctionSpecification, "Auction.findUserAuctionsByCriteria");

        List<AuctionDto> auctionBasicDetailsList = this.auctionMapper.mapToAuctionsDtosDetails(auctionPage.getContent());

        return new PageResponse<>(auctionBasicDetailsList, auctionPage.getNumber() + 1, auctionPage.getTotalPages(), auctionPage.getTotalElements(), auctionPage.getNumberOfElements());

    }

}
