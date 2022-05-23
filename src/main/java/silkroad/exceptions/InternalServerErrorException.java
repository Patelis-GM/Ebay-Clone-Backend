package silkroad.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends SilkRoadException {

    public static final String MEDIA_UPLOAD_FAILURE = "ISEE_001";
    public static final String MEDIA_DELETION_FAILURE = "ISEE_002";
    public static final String MEDIA_RETRIEVAL_FAILURE = "ISEE_003";

    public InternalServerErrorException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public InternalServerErrorException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }
}
