package ru.practicum.shareit.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ObjectMapper objectMapper;
    private final RequestRepository requestRepository;

    @Override
    public List<ItemDto> findAll() {
        List<ItemDto> items = itemRepository.findAll().stream()
                .map(ItemMapper::toItemDto)
                .map(i -> {
                    BookingDto lastBooking = BookingMapper.toBookingDto(
                            bookingRepository.findLastBookingByItem(i.getId(), LocalDateTime.now()).orElse(null)
                    );

                    BookingDto nextBooking = BookingMapper.toBookingDto(
                            bookingRepository.findNextBookingByItem(i.getId(), LocalDateTime.now()).orElse(null)
                    );
                    i.setLastBooking(lastBooking);
                    i.setNextBooking(nextBooking);
                    return i;
                })
                .collect(Collectors.toList());
        return items;
    }

    @Override
    public List<ItemDto> findAllByOwnerId(Long ownerId) {
        List<ItemDto> items = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .map(ItemMapper::toItemDto)
                .map(i -> {
                    BookingDto lastBooking = BookingMapper.toBookingDto(
                            bookingRepository.findLastBookingByItem(i.getId(), LocalDateTime.now()).orElse(null)
                    );

                    BookingDto nextBooking = BookingMapper.toBookingDto(
                            bookingRepository.findNextBookingByItem(i.getId(), LocalDateTime.now()).orElse(null)
                    );

                    List<CommentDto> comments = commentRepository.findAllByItem_Id(i.getId()).stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList());

                    i.setLastBooking(lastBooking);
                    i.setNextBooking(nextBooking);
                    i.setComments(comments);
                    return i;
                })
                .collect(Collectors.toList());

        return items.stream()
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchAvailable(String req) {
        String str = req.toLowerCase();
        List<Item> result = itemRepository.findAll().stream()
                .filter(item -> (item.getName().toLowerCase().contains(str)
                        || item.getDescription().toLowerCase().contains(str))
                        && item.isAvailable() && !str.isEmpty())
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public ItemDto findById(Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404,
                        String.format("Объект с индентификатором %d не найден.", id)));

        BookingDto lastBooking = null;
        BookingDto nextBooking = null;
        Long itemOwnerId = item.getOwner().getId();

        if (userId.equals(itemOwnerId)) {
            lastBooking = BookingMapper.toBookingDto(
                    bookingRepository.findLastBookingByItem(id, LocalDateTime.now()).orElse(null)
            );


            nextBooking = BookingMapper.toBookingDto(
                    bookingRepository.findNextBookingByItem(id, LocalDateTime.now()).orElse(null)
            );
        }

        List<CommentDto> comments = commentRepository.findAllByItem_Id(item.getId()).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        ItemDto response = ItemMapper.toItemDto(item);
        response.setLastBooking(lastBooking);
        response.setNextBooking(nextBooking);
        response.setComments(comments);
        return response;

    }

    @Override
    public Item save(Long ownerId, ItemCreateRequestDto dto) {
        User owner = userService.findById(ownerId);
        Item item = ItemMapper.createRequestDtoToItem(dto);

        if (dto.getRequestId() != null) {
            Request request = requestRepository.findById(dto.getRequestId()).orElse(null);
            item.setRequest(request);
        }

        item.setOwner(owner);

        if (!item.isAvailable()) {
            throw new AvailableException(400, "Объект должен быть доступен");
        }

        return itemRepository.save(item);
    }

    @Override
    public Item update(Long itemId, Long ownerId, String json) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(404,
                        String.format("Объект с индентификатором %d не найден.", itemId)));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new WrongOwnerException("Id владельца не совпадает");
        }

        Item itemToUpdate = Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .available(item.isAvailable())
                .build();

        ObjectReader reader = objectMapper.readerForUpdating(itemToUpdate);
        Item updated;

        try {
            updated = reader.readValue(json);
        } catch (JsonProcessingException e) {
            throw new JsonException("Не удалось считать данные для обновления пользователя!");
        }

        return itemRepository.save(updated);

    }

    @Override
    public CommentDto addComment(String text, Long itemId, Long userId) {
        Booking booking = bookingRepository
                .findBookingByItemIdAndBookerIdAndEndIsBefore(itemId, userId, LocalDateTime.now()).orElse(null);

        if (booking == null) {
            throw new WrongBookingItemException(400, "Пользователь не может оставить комментарий");
        }


        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(404,
                        String.format("Объект с индентификатором %d не найден.", itemId)));

        Comment comment = Comment.builder()
                .text(text)
                .author(userService.findById(userId))
                .item(item)
                .created(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        return CommentMapper.toCommentDto(comment);
    }
}
