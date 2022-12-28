package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByStartDesc(Long userId);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfter(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndEndBefore(Long userId, LocalDateTime start);

    List<Booking> findAllByBooker_IdAndStartIsAfterOrderByStatusDesc(Long userId, LocalDateTime start);

    List<Booking> findAllByBooker_idAndStatusEquals(Long userId, String status);

    @Query("select b from Booking b Inner join Item i on b.item.id = i.id where i.owner.id = ?1 " +
            "order by b.start desc")
    List<Booking> getAllUsersItemsBookings(Long userId);

    @Query("select b from Booking b Inner join Item i on b.item.id = i.id where i.owner.id = ?1 " +
            "and b.start > ?2 order by b.start desc")
    List<Booking> getFutureUsersItemsBookings(Long userId, LocalDateTime nowTime);

    List<Booking> findAllByItemOwnerAndEndBefore(User owner, LocalDateTime end);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(User owner, LocalDateTime start,
                                                              LocalDateTime end);

    List<Booking> findAllByItemOwnerAndStatusEquals(User owner, String status);


    @Query(value = "select * " +
            "from bookings as b join items i on i.id = b.item_id " +
            "where i.id = ?1 and b.end_time < ?2 " +
            "order by b.end_time desc " +
            "limit 1 ", nativeQuery = true)
    Optional<Booking> findLastBookingByItem(long itemId, LocalDateTime time);

    @Query(value = "select * " +
            "from bookings as b join items i on i.id = b.item_id " +
            "where i.id = ?1 and b.start_time > ?2 " +
            "order by b.start_time desc " +
            "limit 1", nativeQuery = true)
    Optional<Booking> findNextBookingByItem(long itemId, LocalDateTime time);

    Optional<Booking> findBookingByItemIdAndBookerIdAndEndIsBefore(long itemId, long userId, LocalDateTime time);



}
