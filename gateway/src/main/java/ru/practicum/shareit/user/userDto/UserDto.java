package ru.practicum.shareit.user.userDto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto
{
    private Long id;

    private String name;

    @Email
    private String email;
}
