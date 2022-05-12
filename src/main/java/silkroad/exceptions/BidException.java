package silkroad.exceptions;

import org.springframework.http.HttpStatus;

public class BidException extends SilkRoadException {


    public static final String BID_INVALID_AMOUNT = "BE_001";
    public static final String BID_HIGHER_BID_EXISTS = "BE_002";

    public BidException(String message, String code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }

    public BidException(String code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }

}
