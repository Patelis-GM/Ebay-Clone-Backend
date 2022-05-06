package silkroad.exceptions;

import org.springframework.http.HttpStatus;

public class AuctionException extends SilkRoadException {

    public static final String NOT_FOUND = "AE_001";
    public static final String INVALID_CATEGORIES = "AE_002";
    public static final String SELLER_BAD_CREDENTIALS = "AE_003";
    public static final String HAS_BID = "AE_004";
    public static final String MODIFIED_OR_EXPIRED = "AE_005";
    public static final String BUYER_BAD_CREDENTIALS = "AE_006";

    public AuctionException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public AuctionException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }

}


