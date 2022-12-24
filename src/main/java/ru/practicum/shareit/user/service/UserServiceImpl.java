package ru.practicum.shareit.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.InMemoryUserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final ObjectMapper objectMapper;

    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User findById(Long id) {
        return inMemoryUserStorage.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        404,
                        String.format("Пользователь с индентификатором %d не найден.", id)));
    }

    public User save(User user) {
        if (emailExists(user)) {
            throw new IllegalStateException("Пользователь с таким email уже существует");
        }

        return inMemoryUserStorage.save(user);
    }

    public User update(Long id, String json) {
        User user = findById(id);

        User userToUpdate = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        ObjectReader reader = objectMapper.readerForUpdating(userToUpdate);
        User updated;

        try {
            updated = reader.readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось считать данные для обновления пользователя!");
        }

        return save(updated);

    }

    public void delete(Long id) {
        inMemoryUserStorage.deleteUser(id);
    }

    private boolean emailExists(User user) {
        return inMemoryUserStorage.findAll().stream()
                .anyMatch(u -> (u.getEmail().equals(user.getEmail()) && !u.getId().equals(user.getId())));
    }
}
