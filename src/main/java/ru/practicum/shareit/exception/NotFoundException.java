package ru.practicum.shareit.exception;

public class NotFoundException extends RuntimeException{
    private int code;

    public NotFoundException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
