package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;


@SpringBootTest
@Transactional(propagation = Propagation.REQUIRED)
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingController bookingController;

    private BookingRequestDto bookingRequestDto;

    private BookingDto bookingDto;

    private Booking booking;
    private User user;
    private User user2;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User(1L, "name", "f@f.ru");
        user2 = new User(2L, "name2", "fsd@dsf.ru");

        item = new Item(1L, "test", "test", true, null, null);

        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 1, 10, 10, 30))
                .end(LocalDateTime.of(2023, 2, 10, 10, 30))
                .status(BookingStatus.WAITING.getS())
                .booker(user)
                .item(item)
                .build();


        bookingRequestDto = BookingRequestDto.builder()
                .start(LocalDateTime.of(2023, 12, 10, 10, 20, 10))
                .end(LocalDateTime.of(2024, 12, 20, 10, 20, 10))
                .itemId(1L)
                .build();

        booking = Booking.builder()
                .start(LocalDateTime.of(2023, 1, 10, 10, 30))
                .end(LocalDateTime.of(2023, 2, 10, 10, 30))
                .item(item)
                .booker(user2)
                .status(WAITING)
                .build();
    }

    @Test
    void approveBooking() {
        User u = userRepository.save(user);
        userRepository.save(user2);

        item.setOwner(u);
        itemRepository.save(item);

        Booking b = bookingRepository.save(booking);
        b.setBooker(u);
        bookingRepository.save(b);

        bookingController.changeStatus(1L, "true", 1L);
        bookingDto.setBooker(u);
        bookingDto.setStatus(BookingStatus.APPROVED.getS());

        BookingDto bookingDto = bookingController.findById(1L, 1L);
        assertEquals(bookingDto.getStatus(), "APPROVED");
    }
}