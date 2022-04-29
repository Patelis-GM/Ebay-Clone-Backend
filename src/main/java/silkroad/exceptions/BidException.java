package silkroad.exceptions;

import org.springframework.http.HttpStatus;

public class BidException extends SilkRoadException {


    public static final String INVALID_AMOUNT = "BE_001";
    public static final String BAD_CREDENTIALS = "BE_002";
    public static final String ALREADY_BOUGHT = "BE_003";
    public static final String HIGHER_BID = "BE_004";

    public BidException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public BidException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }

}
