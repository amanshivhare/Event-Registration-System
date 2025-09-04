package com.example.eventregistration.repository;

import com.example.eventregistration.entity.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldSaveAndFindEventById() {
        Event event = new Event(null, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Event savedEvent = eventRepository.save(event);

        Optional<Event> found = eventRepository.findById(savedEvent.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Hackathon");
    }

    @Test
    void shouldFindAllEvents() {
        Event e1 = new Event(null, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Event e2 = new Event(null, "Conference", LocalDate.now(), "London", "Business event");

        eventRepository.save(e1);
        eventRepository.save(e2);

        List<Event> events = eventRepository.findAll();

        assertThat(events).hasSize(2);
        assertThat(events.get(0).getName()).isIn("Hackathon", "Conference");
    }

    @Test
    void shouldDeleteEvent() {
        Event event = new Event(null, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Event saved = eventRepository.save(event);

        eventRepository.deleteById(saved.getId());

        Optional<Event> found = eventRepository.findById(saved.getId());
        assertThat(found).isNotPresent();
    }
}
