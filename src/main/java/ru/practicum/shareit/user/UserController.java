package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable(name = "userId") Long id) {
        User found = userService.findById(id);
        return UserMapper.toUserDto(found);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody User user) {
        User created = userService.save(user);
        return UserMapper.toUserDto(created);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @PathVariable(name = "userId") final Long id,
            @RequestBody final String json
    ) {
        User updated = userService.update(id, json);
        return UserMapper.toUserDto(updated);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable(name = "userId") Long id) {
        userService.delete(id);
    }



}
