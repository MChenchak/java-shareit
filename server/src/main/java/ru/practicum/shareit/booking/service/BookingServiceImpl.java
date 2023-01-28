package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @Override
    public Booking create(BookingRequestDto request, Long userId) {

        if (request.getEnd().isBefore(request.getStart())) {
            throw new WrongDateException(400, "Дата окончания должна быть позже даты старта");
        }

        if (!isAvailable(request.getItemId(), userId)) {
            throw new WrongBookingItemException(400, "Объект недоступен для бронирования");
        }
        User booker = userService.findById(userId);
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException(404,
                        String.format("Объект с индентификатором %d не найден.", request.getItemId())));

        if (item.getOwner().getId().equals(userId)) {
            throw new WrongOwnerException("Владелец не может бронировать свою вещь");
        }

        Booking toSave = Booking.builder()
                .start(request.getStart())
                .end(request.getEnd())
                .booker(booker)
                .item(item)
                .status(BookingStatus.WAITING)
                .build();

        return bookingRepository.save(toSave);
    }

    @Override
    public Booking changeStatus(Long bookingId, String approved, Long userId) {
        Booking toChange = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(404,
                        String.format("Объект с индентификатором %d не найден.", bookingId)));

        if (!toChange.getItem().getOwner().getId().equals(userId)) {
            throw new WrongOwnerException("Пользователь не может изменить статус");
        }

        if (toChange.getStatus().equals(BookingStatus.APPROVED)) {
            throw new WrongBookingItemException(400, "Нельзя изменить статус");
        }

        if (approved.equals("true")) {
            toChange.setStatus(BookingStatus.APPROVED);
        }

        if (approved.equals("false")) {
            toChange.setStatus(BookingStatus.REJECTED);
        }

        return bookingRepository.save(toChange);
    }

    @Override
    public Booking findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(404,
                        String.format("Объект с индентификатором %d не найден.", bookingId)));
        if (!(booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId))) {
            throw new NotFoundException(404, "Этот пользователь не может просмотреть бронь");
        }

        return booking;
    }

    @Override
    public List<Booking> findBookingsByBookerAndState(Long userId, String state, int from, int size) {
        if (from < 0 || size < 0) {
            throw new BadRequestException("from и size не могут быть отрицательными ");
        }
        userService.findById(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBooker_Id(userId, pageRequest);
            case "CURRENT":
                return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now(), pageRequest);
            case "PAST":
                return bookingRepository.findAllByBooker_IdAndEndBefore(userId, LocalDateTime.now(), pageRequest);
            case "FUTURE":
                return bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStatusDesc(userId, LocalDateTime.now(), pageRequest);
            case "WAITING":
                return bookingRepository.findAllByBooker_idAndStatusEquals(userId, BookingStatus.WAITING, pageRequest);
            case "REJECTED":
                return bookingRepository.findAllByBooker_idAndStatusEquals(userId, BookingStatus.REJECTED, pageRequest);
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<Booking> getAllBokingsByOwnerSortByState(Long userId, String state, int from, int size) {
        if (from < 0 || size < 0) {
            throw new BadRequestException("from и size не могут быть отрицательными ");
        }
        User user = userService.findById(userId);
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "start"));
        List<Booking> allBookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                allBookings.addAll(bookingRepository.getAllUsersItemsBookings(userId, pageRequest));
                break;
            case "CURRENT":
                allBookings.addAll(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(user,
                        LocalDateTime.now(), LocalDateTime.now(), pageRequest));
                break;
            case "PAST":
                allBookings.addAll(bookingRepository.findAllByItemOwnerAndEndBefore(user,
                        LocalDateTime.now(), pageRequest));
                break;
            case "FUTURE":
                allBookings.addAll(bookingRepository.getFutureUsersItemsBookings(userId, LocalDateTime.now(), pageRequest));
                break;
            case "WAITING":
                allBookings.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(user, BookingStatus.WAITING,
                        pageRequest));
                break;
            case "REJECTED":
                allBookings.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(user, BookingStatus.REJECTED,
                        pageRequest));
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }

        return allBookings;

    }


    private boolean isAvailable(Long itemId, Long userId) {
        return itemService.findById(itemId, userId).isAvailable();
    }
}
