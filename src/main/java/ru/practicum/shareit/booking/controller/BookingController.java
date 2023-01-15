package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto findById(
            @PathVariable final Long bookingId,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        Booking booking = bookingService.findById(bookingId, userId);
        return BookingMapper.toBookingDto(booking);
    }

    @GetMapping
    public List<BookingDto> findBookingsByUser(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return bookingService.findBookingsByBookerAndState(userId, state, from, size).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> findBookingsByOwner(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllBokingsByOwnerSortByState(userId, state, from, size).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public BookingDto createBooking(
            @Valid @RequestBody final BookingRequestDto request,
            @RequestHeader(value = "X-Sharer-User-Id") Long id
    ) {
        Booking saved = bookingService.create(request, id);
        return BookingMapper.toBookingDto(saved);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(
            @PathVariable final Long bookingId,
            @RequestParam("approved") final String approved,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId
    ) {
        Booking changed = bookingService.changeStatus(bookingId, approved, userId);
        return BookingMapper.toBookingDto(changed);
    }
}
