package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
@Data
@RequiredArgsConstructor
public class Booking {
    private final Long id;
    private final Instant start;
    private final Instant end;
    private final Item item;
    private final User booker;
    private final String status;

}
