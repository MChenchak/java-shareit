package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemController.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto item;

    @BeforeAll
    void init() {
        item = new ItemDto(1L, "name", "desc", true, 1L, 1L);
    }

    @Test
    void findById() throws Exception {
        when(itemService.findById(anyLong(), anyLong()))
                .thenReturn(item);

        mockMvc.perform(
                        get("/items/1")
                                .param("itemId", "1")
                                .header("X-Sharer-User-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(item.getName())));
    }

    @Test
    void findAll() throws Exception {
        when(itemService.findAllByOwnerId(anyLong()))
                .thenReturn(List.of(item));

        mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void createItem() throws Exception {
        ItemCreateRequestDto itemCreateDto = new ItemCreateRequestDto(
                "name",
                "ssss",
                true,
                1L
        );

        when(itemService.save(1L, itemCreateDto))
                .thenReturn(mock(Item.class));

        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(itemCreateDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createComment() throws Exception {
        Comment comment = new Comment(
                1L,
                "commenttt",
                new User(),
                new Item(),
                now()
        );

        CommentDto dto = new CommentDto(1L, "commenttt", 1L, "name", now());

        when(itemService.addComment(any(), anyLong(), anyLong()))
                .thenReturn(dto);

        mockMvc.perform(
                        post("/items/1/comment")
                                .header("X-Sharer-User-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("itemId", "1")
                                .content(objectMapper.writeValueAsString(comment))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(dto.getText())));
    }

    @Test
    void searchAvailable() throws Exception {
        when(itemService.searchAvailable(anyString()))
                .thenReturn(List.of(new Item()));

        mockMvc.perform(
                        get("/items/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("text", "1")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.update(anyLong(), anyLong(), anyString()))
                .thenReturn(new Item(1L, "name", "desc", true, new User(), new Request()));

        String json = "{\"name\": \"newName\"}";

        mockMvc.perform(
                        patch("/items/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .param("itemId", "1")
                                .header("X-Sharer-User-Id", "1")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name")));

    }

}
