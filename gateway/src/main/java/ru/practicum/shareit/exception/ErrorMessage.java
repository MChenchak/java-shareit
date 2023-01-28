package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class ErrorMessage {
    private String error;

    public ErrorMessage(String error) {
        this.error = error;
    }
}
