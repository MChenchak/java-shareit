package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService requestService;


    @PostMapping
    public Request add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @RequestBody Request request) {


        return requestService.addRequest(userId, request);
    }


    @GetMapping("/all")
    public List<RequestDto> getAllWithItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        return requestService.getAllRequestsWithItems(userId, from, size);
    }

    @GetMapping()
    public List<RequestDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getAllRequestsByUser(userId);
    }


    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long requestId) {

        return requestService.getRequestById(userId, requestId);
    }
}
