package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDto> findAll();

    ItemDto findById(Long id, Long userId);

    List<ItemDto> findAllByOwnerId(Long ownerId);

    List<Item> searchAvailable(String req);

    Item save(Long ownerId, Item item);

    Item update(Long id, Long ownerId, String json);

    CommentDto addComment(String text, Long itemId, Long userId);
}
