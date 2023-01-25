package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class ItemCreateRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private boolean available;
    private Long requestId;
}
