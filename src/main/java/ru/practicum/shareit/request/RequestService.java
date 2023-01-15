package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    Request addRequest(long userId, Request request);

    List<RequestDto> getAllRequestsWithItems(long userId, int from, int size);

    List<RequestDto> getAllRequestsByUser(long userId);

    RequestDto getRequestById(Long userId, Long requestId);
}
