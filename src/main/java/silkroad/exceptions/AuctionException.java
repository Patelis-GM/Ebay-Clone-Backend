package silkroad.exceptions;

import org.springframework.http.HttpStatus;

public class AuctionException extends SilkRoadException {

    public static final String NOT_FOUND = "AE_001";
    public static final String INVALID_CATEGORIES = "AE_002";
    public static final String BAD_CREDENTIALS = "AE_003";
    public static final String EXPIRED = "AE_004";

    public AuctionException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public AuctionException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }

}


