package com.example.eventregistration.service;

import com.example.eventregistration.entity.Event;
import com.example.eventregistration.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
    }

    // ✅ Test: getAll
    @Test
    void shouldReturnAllEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

        List<Event> result = eventService.getAll();

        assertEquals(1, result.size());
        assertEquals("Hackathon", result.get(0).getName());
    }

    // ✅ Test: getById
    @Test
    void shouldReturnEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.getById(1L);

        assertNotNull(result);
        assertEquals("Hackathon", result.getName());
    }

    // ✅ Test: create
    @Test
    void shouldCreateEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event result = eventService.create(event);

        assertNotNull(result);
        assertEquals("Hackathon", result.getName());
        verify(eventRepository, times(1)).save(event);
    }

    // ✅ Test: update
    @Test
    void shouldUpdateEvent() {
        Event updated = new Event(1L, "Hackathon 2.0", LocalDate.now(), "Berlin", "Updated event");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updated);

        Event result = eventService.update(1L, updated);

        assertEquals("Hackathon 2.0", result.getName());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    // ✅ Test: delete
    @Test
    void shouldDeleteEvent() {
        doNothing().when(eventRepository).deleteById(1L);

        String result = eventService.delete(1L);

        assertEquals("Deleted", result);
        verify(eventRepository, times(1)).deleteById(1L);
    }
}
