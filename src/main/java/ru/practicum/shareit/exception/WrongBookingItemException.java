package ru.practicum.shareit.exception;

public class WrongBookingItemException extends RuntimeException{
    private int code;

    public WrongBookingItemException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
