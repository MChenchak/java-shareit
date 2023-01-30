package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ErrorMessage> handleNotFoundException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage()));
    }


    @ExceptionHandler({
            WrongOwnerException.class
    })
    public ResponseEntity<ErrorMessage> handleWrongOwnerException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler({
            AvailableException.class,
            WrongBookingItemException.class,
            WrongDateException.class,
    })
    public ResponseEntity<ErrorMessage> handleBadRequest(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler({
            BadRequestException.class
    })
    public ResponseEntity<ErrorMessage> handleWrongState(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }

}
