package silkroad.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryException extends SilkRoadException {

    public static final String INVALID_CATEGORIES = "CE001";

    public CategoryException(String category, String code, HttpStatus httpStatus) {
        super(category, code, httpStatus);
    }

}


