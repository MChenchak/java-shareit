package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> findAll();

    Item findById(Long id);

    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> searchAvailable(String req);

    Item save(Long ownerId, Item item);

    Item update(Long id, Long ownerId, String json);
}
