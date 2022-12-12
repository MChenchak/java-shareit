package ru.practicum.shareit.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
