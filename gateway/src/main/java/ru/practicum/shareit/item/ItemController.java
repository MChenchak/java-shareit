package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.Comment;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestBody ItemDto item) {
        return itemClient.createItem(userId, item);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.findItemById(id, userId);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> put(@PathVariable long itemId,
                                      @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestBody String json) {
        return itemClient.updateItem(userId, itemId, json);
    }


    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String req) {
        return itemClient.search(req);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @PathVariable(name = "itemId") Long itemId,
            @Valid @RequestBody Comment comment,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        return itemClient.addComment(itemId, userId, comment);

    }

}
