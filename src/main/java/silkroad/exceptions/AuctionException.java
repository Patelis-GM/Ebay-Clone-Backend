package silkroad.exceptions;

import org.springframework.http.HttpStatus;

public class AuctionException extends SilkRoadException {

    public static final String AUCTION_NOT_FOUND = "AE_001";
    public static final String AUCTION_INVALID_CATEGORIES = "AE_002";
    public static final String AUCTION_HAS_BID_OR_EXPIRED = "AE_003";
    public static final String AUCTION_MODIFIED_OR_EXPIRED = "AE_004";
    public static final String AUCTION_INVALID_FIRST_BID = "AE_005";
    public static final String AUCTION_INVALID_BUY_PRICE = "AE_006";
    public static final String AUCTION_INVALID_BUY_PRICE_FIRST_BID = "AE_007";

    public AuctionException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public AuctionException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }

}


