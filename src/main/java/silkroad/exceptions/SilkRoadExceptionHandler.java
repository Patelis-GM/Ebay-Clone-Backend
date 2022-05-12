package silkroad.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SilkRoadExceptionHandler {

    @ExceptionHandler(value = SilkRoadException.class)
    public ResponseEntity<SilkRoadExceptionDetails> handleInvalidInputException(SilkRoadException exception) {
        return new ResponseEntity<>(new SilkRoadExceptionDetails(exception), exception.getHttpStatus());
    }

}