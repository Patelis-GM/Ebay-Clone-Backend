package silkroad.services;

import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import silkroad.entities.Auction;
import silkroad.entities.Image;
import silkroad.exceptions.InternalServerErrorException;
import silkroad.repositories.ImageRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ImageService {

    private final String UPLOADS_DIRECTORY = "Uploads/";
    private final String PLACEHOLDER_IMAGE_PATH = "src/main/resources/PlaceholderImage.png";
    private final ImageRepository imageRepository;

    @Transactional
    public void uploadImages(Auction auction, MultipartFile[] multipartFiles) {

        File uploadsDirectory = new File(this.UPLOADS_DIRECTORY);

        if (!uploadsDirectory.exists())
            uploadsDirectory.mkdir();

        File auctionDirectory = new File(this.UPLOADS_DIRECTORY + auction.getId() + "/");

        if (!auctionDirectory.exists())
            auctionDirectory.mkdir();

        List<Image> auctionImages = new ArrayList<>();

        if (multipartFiles != null)
            for (MultipartFile multipartFile : multipartFiles) {

                String fileName = multipartFile.getOriginalFilename();
                String filePath = this.UPLOADS_DIRECTORY + auction.getId() + "/" + fileName;

                File file = new File(filePath);

                try {
                    if (file.createNewFile()) {

                        try {

                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(multipartFile.getBytes());
                            fileOutputStream.close();
                            auctionImages.add(new Image(filePath, auction));

                        } catch (IOException e) {
                            throw new InternalServerErrorException(e.getMessage(), InternalServerErrorException.MEDIA_UPLOAD_FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                } catch (IOException e) {
                    throw new InternalServerErrorException(e.getMessage(), InternalServerErrorException.MEDIA_UPLOAD_FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

        else {
            File multipartFile = new File(PLACEHOLDER_IMAGE_PATH);
            String fileName = multipartFile.getName();
            String filePath = this.UPLOADS_DIRECTORY + auction.getId() + "/" + fileName;
            File auctionFile = new File(filePath);
            try {
                Files.copy(multipartFile.toPath(), auctionFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                auctionImages.add(new Image(filePath, auction));
            } catch (IOException e) {
                throw new InternalServerErrorException(e.getMessage(), InternalServerErrorException.MEDIA_UPLOAD_FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        this.imageRepository.saveAll(auctionImages);
    }

    @Transactional
    public void updateImages(Auction auction, MultipartFile[] multipartFiles) {

        File auctionDirectory = new File(this.UPLOADS_DIRECTORY + auction.getId() + "/");

        if (auctionDirectory.exists()) {
            for (File file : Objects.requireNonNull(auctionDirectory.listFiles()))
                file.delete();

            this.imageRepository.deleteByAuction(auction.getId());
        }

        this.uploadImages(auction, multipartFiles);
    }

    @Transactional
    public void deleteImages(Long auctionID, boolean inDatabase) {

        if (inDatabase)
            this.imageRepository.deleteByAuction(auctionID);

        else {

            File auctionDirectory = new File(this.UPLOADS_DIRECTORY + auctionID + "/");

            if (auctionDirectory.exists())
                try {
                    FileUtils.deleteDirectory(auctionDirectory);

                } catch (IOException e) {
                    throw new InternalServerErrorException(e.getMessage(), InternalServerErrorException.MEDIA_DELETION_FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }
    }

    public byte[] getImage(Long auctionID, String fileName) {

        String filePath = this.UPLOADS_DIRECTORY + auctionID + "/" + fileName;
        File file = new File(filePath);
        if (!file.exists())
            return null;
        else {
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                throw new InternalServerErrorException(e.getMessage(), InternalServerErrorException.MEDIA_RETRIEVAL_FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
