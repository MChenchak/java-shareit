package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.Request;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient ItemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Valid @RequestBody Request request) {
        return ItemRequestClient.addRequest(userId, request);
    }


    @GetMapping("/all")
    public ResponseEntity<Object> getAllWithItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        return ItemRequestClient.getAllRequestsWithItems(userId, from, size);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemRequestClient.getAllRequestsByUser(userId);
    }


    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long requestId) {

        return ItemRequestClient.getRequestById(userId, requestId);
    }
}
