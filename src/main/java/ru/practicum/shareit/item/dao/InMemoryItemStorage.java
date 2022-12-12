package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(++id);
        }

        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public void deleteUser(Long id) {
        items.remove(id);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }
}
