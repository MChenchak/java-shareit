package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable(name = "itemId") Long itemId,
                            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        return itemService.findById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemService.findAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailable(@RequestParam("text") String req) {
        return itemService.searchAvailable(req).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto createItem(
            @Valid @RequestBody final ItemCreateRequestDto item,
            @RequestHeader(value = "X-Sharer-User-Id") Long id
    ) {
        Item toSave = itemService.save(id, item);
        return ItemMapper.toItemDto(toSave);

    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable(name = "itemId") Long id,
                              @RequestBody final String json,
                              @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        Item updated = itemService.update(id, ownerId, json);
        return ItemMapper.toItemDto(updated);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createComment(
            @PathVariable(name = "itemId") Long itemId,
            @Valid @RequestBody Comment comment,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        return itemService.addComment(comment.getText(), itemId, userId);

    }
}
