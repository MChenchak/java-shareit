package ru.practicum.shareit.booking.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class BookingDto {
    private final Long id;
    private final Long bookerId;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Item item;
    private final User booker;
    private final String status;
}
