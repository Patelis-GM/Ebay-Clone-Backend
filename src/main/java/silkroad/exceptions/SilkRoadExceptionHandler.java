package silkroad.exceptions;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SilkRoadExceptionHandler {

    @ExceptionHandler(value = SilkRoadException.class)
    public ResponseEntity<SilkRoadExceptionDTO> handleInvalidInputException(SilkRoadException exception) {
        return new ResponseEntity<>(new SilkRoadExceptionDTO(exception), exception.getHttpStatus());
    }

//    @ExceptionHandler(value = CannotAcquireLockException.class)
//    public ResponseEntity<SilkRoadExceptionDTO> handleInvalidInputExceptio(CannotAcquireLockException exception) {
//        return new ResponseEntity<>(new SilkRoadExceptionDTO(exception, HttpStatus.OK), HttpStatus.OK);
//    }

}