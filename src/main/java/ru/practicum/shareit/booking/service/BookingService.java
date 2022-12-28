package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {
    Booking create(BookingRequestDto request, Long userId);

    Booking changeStatus(Long bookingId, String approved, Long userId);

    Booking findById(Long bookingId, Long userId);

    List<Booking> findBookingsByBookerAndState(Long ownerId, String state);

    List<Booking> getAllBokingsByOwnerSortByState(Long userId, String state);
}
