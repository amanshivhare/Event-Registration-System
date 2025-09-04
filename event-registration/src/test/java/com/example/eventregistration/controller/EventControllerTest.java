package com.example.eventregistration.controller;

import com.example.eventregistration.config.AppConfig;
import com.example.eventregistration.config.SecurityConfig;
import com.example.eventregistration.dto.request.EventReqDTO;
import com.example.eventregistration.dto.response.EventResDTO;
import com.example.eventregistration.security.JwtUtil;
import com.example.eventregistration.service.impl.MyUserDetailsService;
import com.example.eventregistration.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
    private EventServiceImpl eventServiceImpl;

    @Test
    void shouldReturnAllEvents() throws Exception {
        EventResDTO eventResDTO = new EventResDTO(
                1L,
                "Hackathon",
                LocalDate.now(),
                "Berlin",
                "Tech event",
                null
        );

        when(eventServiceImpl.getAll()).thenReturn(Arrays.asList(eventResDTO));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hackathon"));
    }

    @Test
    void shouldReturnEventById() throws Exception {
        EventResDTO eventResDTO = new EventResDTO(
                1L,
                "Hackathon",
                LocalDate.now(),
                "Berlin",
                "Tech event",
                null
        );

        when(eventServiceImpl.getById(1L)).thenReturn(eventResDTO);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hackathon"))
                .andExpect(jsonPath("$.location").value("Berlin"))
                .andExpect(jsonPath("$.description").value("Tech event"));
    }

    @Test
    void shouldCreateEvent() throws Exception {
        EventResDTO eventResDTO = new EventResDTO(
                1L,
                "Hackathon",
                LocalDate.of(2025, 9, 4),
                "Berlin",
                "Tech event",
                null
        );

        when(eventServiceImpl.create(any(EventReqDTO.class))).thenReturn(eventResDTO);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Hackathon\",\"date\":\"2025-09-04\",\"location\":\"Berlin\",\"description\":\"Tech event\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hackathon"))
                .andExpect(jsonPath("$.location").value("Berlin"))
                .andExpect(jsonPath("$.description").value("Tech event"));
    }

    @Test
    void shouldUpdateEvent() throws Exception {
        EventResDTO updatedEventResDTO = new EventResDTO(
                1L,
                "Hackathon 2.0",
                LocalDate.of(2025, 9, 4),
                "Berlin",
                "Updated",
                null
        );

        when(eventServiceImpl.update(any(Long.class), any(EventReqDTO.class))).thenReturn(updatedEventResDTO);

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Hackathon 2.0\",\"date\":\"2025-09-04\",\"location\":\"Berlin\",\"description\":\"Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hackathon 2.0"))
                .andExpect(jsonPath("$.location").value("Berlin"))
                .andExpect(jsonPath("$.description").value("Updated"));
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        // since delete() is void now, just doNothing
        doNothing().when(eventServiceImpl).delete(1L);

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
