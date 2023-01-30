package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookingController.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;

    @Test
    void findById() throws Exception {
        when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(new Booking(
                        1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        new Item(),
                        new User(),
                        BookingStatus.WAITING));

        mockMvc.perform(
                        get("/bookings/1")
                                .param("bookingId", "1")
                                .header("X-Sharer-User-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void findBookingsByUser() throws Exception {
        when(bookingService.findBookingsByBookerAndState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(new Booking(
                        1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        new Item(),
                        new User(),
                        BookingStatus.WAITING)));

        mockMvc.perform(
                        get("/bookings")
                                .header("X-Sharer-User-Id", "1")
                                .param("from", "1")
                                .param("size", "2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void findBookingsByOwner() throws Exception {
        when(bookingService.findBookingsByBookerAndState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(new Booking(
                        1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        new Item(),
                        new User(),
                        BookingStatus.WAITING)));

        mockMvc.perform(
                        get("/bookings")
                                .header("X-Sharer-User-Id", "1")
                                .param("from", "1")
                                .param("size", "2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.create(any(), anyLong()))
                .thenReturn(new Booking(
                        1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        new Item(),
                        new User(),
                        BookingStatus.WAITING));

        BookingRequestDto bookingRequestDto =
                new BookingRequestDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookingRequestDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


}