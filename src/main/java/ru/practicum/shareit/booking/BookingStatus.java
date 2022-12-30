package ru.practicum.shareit.booking;

public enum BookingStatus {
    WAITING("WAITING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    CANCELED("CANCELED");

    private final String s;

    BookingStatus(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }
}
