package ru.practicum.shareit.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.WrongOwnerException;
import ru.practicum.shareit.item.dao.InMemoryItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final InMemoryItemStorage inMemoryItemStorage;
    private final ObjectMapper objectMapper;

    @Override
    public List<Item> findAll() {
        return inMemoryItemStorage.findAll();
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        return inMemoryItemStorage.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchAvailable(String req) {
        String str = req.toLowerCase();
        List<Item> result = inMemoryItemStorage.findAll().stream()
                .filter(item -> (item.getName().toLowerCase().contains(str)
                        || item.getDescription().toLowerCase().contains(str))
                        && item.isAvailable() && !str.isEmpty())
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public Item findById(Long id) {
        return inMemoryItemStorage.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Объект с индентификатором %d не найден.", id)));
    }

    @Override
    public Item save(Long ownerId, Item item) {
        User owner = userService.findById(ownerId);
        item.setOwner(owner);

        return inMemoryItemStorage.save(item);
    }

    @Override
    public Item update(Long itemId, Long ownerId, String json) {
        Item item = findById(itemId);

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new WrongOwnerException("Id владельца не совпадает");
        }

        Item itemToUpdate = Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .available(item.isAvailable())
                .build();

        ObjectReader reader = objectMapper.readerForUpdating(itemToUpdate);
        Item updated;

        try {
            updated = reader.readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось считать данные для обновления пользователя!");
        }

        return inMemoryItemStorage.save(updated);

    }
}
