package com.example.eventregistration.controller;

import com.example.eventregistration.config.AppConfig;
import com.example.eventregistration.config.SecurityConfig;
import com.example.eventregistration.entity.Event;
import com.example.eventregistration.security.JwtUtil;
import com.example.eventregistration.security.MyUserDetailsService;
import com.example.eventregistration.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
@Import({AppConfig.class, SecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private MyUserDetailsService myUserDetailsService;

    @MockitoBean
    private EventService eventService;

    @Test
    void shouldReturnAllEvents() throws Exception {
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        when(eventService.getAll()).thenReturn(Arrays.asList(event));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hackathon"));
    }

    @Test
    void shouldReturnEventById() throws Exception {
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        when(eventService.getById(1L)).thenReturn(event);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hackathon"));
    }

    @Test
    void shouldCreateEvent() throws Exception {
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        when(eventService.create(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Hackathon\",\"date\":\"2025-09-04\",\"location\":\"Berlin\",\"description\":\"Tech event\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hackathon"));
    }

    @Test
    void shouldUpdateEvent() throws Exception {
        Event updatedEvent = new Event(1L, "Hackathon 2.0", LocalDate.now(), "Berlin", "Updated");
        when(eventService.update(any(Long.class), any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Hackathon 2.0\",\"date\":\"2025-09-04\",\"location\":\"Berlin\",\"description\":\"Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hackathon 2.0"));
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        when(eventService.delete(1L)).thenReturn("Deleted");

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }
}
