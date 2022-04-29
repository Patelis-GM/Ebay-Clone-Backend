package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import silkroad.dtos.auction.request.AuctionStoreDTO;
import silkroad.entities.*;
import silkroad.exceptions.AuctionException;
import silkroad.repositories.*;

import java.util.*;

@Service
@AllArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    @Transactional
    public void createAuction(String username, AuctionStoreDTO auctionDTO, MultipartFile[] multipartFiles) {

        Set<Category> auctionCategories = this.categoryRepository.findAll(auctionDTO.getCategories());

        if (auctionCategories.size() != auctionDTO.getCategories().size())
            throw new AuctionException(auctionDTO.getCategories().toString(), AuctionException.INVALID_CATEGORIES, HttpStatus.BAD_REQUEST);

        User seller = new User(username);

        Auction auction = new Auction(auctionDTO.getName(), auctionDTO.getDescription(), auctionDTO.getEndDate(), auctionDTO.getBuyPrice(), auctionDTO.getFirstBid(), seller);

        auction.setCategories(auctionCategories);

        auction.setAddress(auctionDTO.getAddress());

        this.auctionRepository.save(auction);

//        this.imageService.uploadImages(auction, multipartFiles);
    }

    @Transactional
    public void updateAuction(String username, Long auctionID, AuctionStoreDTO auctionDTO, MultipartFile[] multipartFiles) {

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
        auction.setVersion(auction.getVersion() + 1);
        this.auctionRepository.save(auction);

//        this.imageService.updateImages(auction, multipartFiles);
    }

    @Transactional
    public void deleteAuction(String username, Long auctionID) {

        Optional<Auction> optionalAuction = this.auctionRepository.findByIdWithPessimisticLock(auctionID);

        if (optionalAuction.isEmpty())
            throw new AuctionException(auctionID.toString(), AuctionException.NOT_FOUND, HttpStatus.NOT_FOUND);

        Auction auction = optionalAuction.get();

        if (!auction.getSeller().getUsername().equals(username))
            throw new AuctionException(auctionID.toString(), AuctionException.BAD_CREDENTIALS, HttpStatus.FORBIDDEN);

//        this.imageService.deleteImages(auctionID, true);

        if (this.auctionRepository.removeById(auctionID) == 0)
            throw new AuctionException(auctionID.toString(), AuctionException.AT_LEAST_ONE_BID, HttpStatus.BAD_REQUEST);
//        else
//            this.imageService.deleteImages(auctionID, false);
    }

    public List<Auction> f() {
        AuctionSpecification auctionSpecification = new AuctionSpecification();
//        auctionSpecification.addSearchCriterion(new AuctionSearchCriteria("highestBid", 2, AuctionSearchOperation.GREATER_THAN_OR_EQUAL));
//        auctionSpecification.addSearchCriterion(new AuctionSearchCriteria("highestBid", 100, AuctionSearchOperation.LESS_THAN_OR_EQUAL));
        auctionSpecification.addSearchCriterion(new AuctionSearchCriteria("highestBid", 100, AuctionSearchOperation.MEMBER_OF));
        auctionSpecification.addSearchCriterion(new AuctionSearchCriteria("highestBid", 100, AuctionSearchOperation.LIKE));
        auctionSpecification.addSearchCriterion(new AuctionSearchCriteria("highestBid", 100, AuctionSearchOperation.LOCATION));
        PageRequest pageRequest = PageRequest.of(0, 3);
        return this.auctionRepository.findAll(auctionSpecification, pageRequest).getContent();

    }


}
