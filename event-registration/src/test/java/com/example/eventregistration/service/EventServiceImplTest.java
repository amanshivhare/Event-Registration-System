package com.example.eventregistration.service;

import com.example.eventregistration.dto.request.EventReqDTO;
import com.example.eventregistration.dto.response.EventResDTO;
import com.example.eventregistration.exception.exceptions.ApiRequestException;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.repository.EventRepository;
import com.example.eventregistration.service.impl.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    private Event event;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
    }

    @Test
    void shouldReturnAllEvents() {
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));
        List<EventResDTO> result = eventServiceImpl.getAll();
        assertEquals(1, result.size());
        assertEquals("Hackathon", result.get(0).getName());
        assertEquals("Berlin", result.get(0).getLocation());
        assertEquals("Tech event", result.get(0).getDescription());
    }

    @Test
    void shouldReturnEventById() {
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        EventResDTO result = eventServiceImpl.getById(1L);
        assertNotNull(result);
        assertEquals("Hackathon", result.getName());
        assertEquals("Berlin", result.getLocation());
        assertEquals("Tech event", result.getDescription());
    }

    @Test
    void shouldCreateEvent() {
        EventReqDTO req = new EventReqDTO(
                "Hackathon",
                LocalDate.of(2025, 9, 4),
                "Berlin",
                "Tech event"
        );
        Event saved = new Event(1L, "Hackathon",
                LocalDate.of(2025, 9, 4), "Berlin", "Tech event");
        when(eventRepository.save(any(Event.class))).thenReturn(saved);
        EventResDTO result = eventServiceImpl.create(req);
        assertNotNull(result);
        assertEquals("Hackathon", result.getName());
        assertEquals("Berlin", result.getLocation());
        assertEquals("Tech event", result.getDescription());
        assertEquals(LocalDate.of(2025, 9, 4), result.getDate());
    }

    @Test
    void shouldUpdateEvent() {
        Event existing = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Event updated = new Event(1L, "Hackathon 2.0", LocalDate.now(), "Berlin", "Updated event");
        EventReqDTO updateReq = new EventReqDTO(
                "Hackathon 2.0",
                LocalDate.now(),
                "Berlin",
                "Updated event"
        );
        when(eventRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenReturn(updated);
        EventResDTO result = eventServiceImpl.update(1L, updateReq);
        assertNotNull(result);
        assertEquals("Hackathon 2.0", result.getName());
        assertEquals("Updated event", result.getDescription());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void shouldDeleteEvent() {
        when(eventRepository.existsById(1L)).thenReturn(true);
        doNothing().when(eventRepository).deleteById(1L);
        eventServiceImpl.delete(1L);
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFoundOnDelete() {
        when(eventRepository.existsById(1L)).thenReturn(false);
        ApiRequestException ex = assertThrows(
                ApiRequestException.class,
                () -> eventServiceImpl.delete(1L)
        );
        assertEquals("Event not found with id: 1", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        verify(eventRepository, never()).deleteById(anyLong());
    }
}
