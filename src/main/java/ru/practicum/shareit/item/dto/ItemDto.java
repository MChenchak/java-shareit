package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
public class ItemDto {

    private final Long id;
    private final String name;
    private final String description;
    private final boolean available;
    private final Long ownerId;
    private final Long requestId;
}
