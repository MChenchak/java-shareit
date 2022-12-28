package ru.practicum.shareit.booking;

public enum BookingStatus {
    WAITING("waiting"),
    APPROVED("true"),
    REJECTED("false"),
    CANCELED("canceled");

    private final String s;

    BookingStatus(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }
}
