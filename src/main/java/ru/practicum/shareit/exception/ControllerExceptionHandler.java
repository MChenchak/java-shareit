package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<?> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler({
            WrongOwnerException.class
    })
    public ResponseEntity<?> handleWrongOwnerException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({
            AvailableException.class,
            WrongBookingItemException.class,
            WrongDateException.class,
    })
    public ResponseEntity<?> handleBadRequest() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({
            BadRequestException.class
    })
    public ResponseEntity<ErrorMessage> handleWrongState() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage("Unknown state: UNSUPPORTED_STATUS"));
    }

}
