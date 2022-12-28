package ru.practicum.shareit.exception;

public class AvailableException extends RuntimeException {
    private int code;

    public AvailableException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
