package ru.practicum.shareit.exception;

public class WrongDateException extends RuntimeException {
    private final int code;

    public WrongDateException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
