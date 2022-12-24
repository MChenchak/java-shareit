package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@RequiredArgsConstructor
public class ItemRequest {
    private final Long id;
    private final String description;
    private final User requestor;
    private final Instant created;
}
