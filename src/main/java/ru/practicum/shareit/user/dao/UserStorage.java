package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User save(User user);

    Optional<User> findById(Long id);

    void deleteUser(Long id);

    List<User> findAll();

}
