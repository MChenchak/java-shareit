package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ItemCreateRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private boolean available;
    private Long requestId;
}
