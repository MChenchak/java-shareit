package ru.practicum.shareit.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.JsonException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        404,
                        String.format("Пользователь с индентификатором %d не найден.", id)));
    }

    public User save(User user) {

        return userRepository.save(user);
    }

    @Transactional
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
            throw new JsonException("Не удалось считать данные для обновления пользователя!");
        }
        updated.setId(id);
        return save(updated);

    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
