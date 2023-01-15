package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final ItemRepository itemRepository;
    @Override
    public Request addRequest(long userId, Request request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(400, "Не найден пользователь с id: " + userId));

        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        requestRepository.save(request);
        return request;
    }

    @Override
    public List<RequestDto> getAllRequestsWithItems(long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(400, "Не найден пользователь с id: " + userId));
        List<Request> requests = requestRepository
                .getRequestsByItemOwner(userId, PageRequest.of(
                        from,
                        size,
                        Sort.by(Sort.Direction.DESC, "created"))).orElse(null);

        if (requests == null) {
            return Collections.emptyList();
        }

        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .map(requestDto -> {
                    List<ItemDtoForRequest> items = itemRepository.findAllByRequestId(requestDto.getId()).stream()
                            .map(ItemMapper::itemToItemForRequest)
                            .collect(Collectors.toList());
                    requestDto.setItems(items);
                    return requestDto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<RequestDto> getAllRequestsByUser(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(400, "Не найден пользователь с id: " + userId));
        List<Request> requests = requestRepository.getAllByRequestorId(userId);

        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .map(requestDto -> {
                    List<ItemDtoForRequest> items = itemRepository.findAllByRequestId(requestDto.getId()).stream()
                            .map(ItemMapper::itemToItemForRequest)
                            .collect(Collectors.toList());
                    requestDto.setItems(items);
                    return requestDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto getRequestById(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(400, "Не найден пользователь с id: " + userId));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(400, "не найден запрос с id: " + requestId));

        List<ItemDtoForRequest> itemsList = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::itemToItemForRequest)
                .collect(Collectors.toList());
        RequestDto requestDto = RequestMapper.toRequestDto(request);
        requestDto.setItems(itemsList);
        return requestDto;
    }
}
