//package ru.practicum.shareit.request;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.repository.ItemRepository;
//import ru.practicum.shareit.user.model.User;
//import ru.practicum.shareit.user.repository.UserRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.equalTo;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//class RequestRepositoryTest {
//    @Autowired
//    private RequestRepository itemRequestRepository;
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void findAllByRequestorIdOrderByCreatedAscTest() {
//        User user = User.builder()
//                .id(1L)
//                .name("name")
//                .email("f@d.ru")
//                .build();
//
//        user = userRepository.save(user);
//
//        Request request = Request.builder()
//                .description("description")
//                .requestor(user)
//                .created(LocalDateTime.now())
//                .build();
//
//        itemRequestRepository.save(request);
//        List<Request> items = itemRequestRepository.getAllByRequestorId(user.getId());
//        assertThat(items.size(), equalTo(1));
//    }
//
//
//    @Test
//    void findAllusersRequestIdOrder() {
//        User user = new User(1L, "name1", "e@d.ru");
//        User user2 = new User(2L, "name2", "ed@d.ru");
//        User user3 = new User(3L, "name3", "efdfdfd@d.ru");
//
//        user = userRepository.save(user);
//        user2 = userRepository.save(user2);
//        user3 = userRepository.save(user3);
//
//        Request request = Request.builder()
//                .description("description1")
//                .requestor(user)
//                .created(LocalDateTime.now())
//                .build();
//        Request request2 = Request.builder()
//                .description("description2")
//                .requestor(user2)
//                .created(LocalDateTime.now())
//                .build();
//        Request request3 = Request.builder()
//                .description("description3")
//                .requestor(user3)
//                .created(LocalDateTime.now())
//                .build();
//
//        itemRequestRepository.save(request);
//        itemRequestRepository.save(request2);
//        itemRequestRepository.save(request3);
//
//        Item item = new Item(1L, "test", "test", true, user, request);
//        Item item2 = new Item(2L, "test2", "test2", true, user, request2);
//        itemRepository.save(item);
//        itemRepository.save(item2);
//
//        Optional<List<Request>> items = itemRequestRepository.getRequestsByItemOwner(1L, PageRequest.of(0, 20));
//        List<Request> itemsExp = List.of(request2);
//        assertEquals(items.get().size(), itemsExp.size());
//    }
//
//
//}