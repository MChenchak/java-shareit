package ru.practicum.shareit.item.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional(propagation = Propagation.REQUIRED)
class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestRepository requestRepository;

    private ItemCreateRequestDto itemToCreate;

    private User owner1;
    private User requestor;
    private Booking booking1;


    @BeforeAll
    void init() {
        itemRepository.deleteAll();
        owner1 = User.builder()
                .name("User-1")
                .email("d@ma.ru")
                .build();

        requestor = User.builder()
                .name("User-requestor")
                .email("request@ma.ru")
                .build();

        itemToCreate = ItemCreateRequestDto.builder()
                .name("item-1")
                .description("description-1")
                .available(true)
                .requestId(null)
                .build();

//        booking1 = Booking.builder()
//                .start(LocalDateTime.now())
//                .end(LocalDateTime.now().plusDays(1L))
//                .item(item1)
//                .booker(requestor)
//                .build();

    }

    @Test
    void findAll() throws Exception {
        userService.save(owner1);
        userService.save(requestor);

        itemService.save(owner1.getId(), itemToCreate);

        List<ItemDto> result = itemService.findAll();

        assertEquals(result.size(), 1);

    }

    @Test
    void findAllByOwnerId() {
        User owner = userService.save(owner1);
        userService.save(requestor);

        itemService.save(owner.getId(), itemToCreate);

        List<ItemDto> result = itemService.findAllByOwnerId(owner.getId());

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getName(), "item-1");
    }


}