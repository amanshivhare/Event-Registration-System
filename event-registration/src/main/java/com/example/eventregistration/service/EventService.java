package com.example.eventregistration.service;

import com.example.eventregistration.entity.Event;
import com.example.eventregistration.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(@PathVariable Long id) {
        return eventRepository.findById(id).orElseThrow();
    }

    public Event create(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    public Event update(@PathVariable Long id, @RequestBody Event event) {
        Event existing = eventRepository.findById(id).orElseThrow();
        existing.setName(event.getName());
        existing.setDate(event.getDate());
        existing.setLocation(event.getLocation());
        existing.setDescription(event.getDescription());
        return eventRepository.save(existing);
    }

    public String delete(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "Deleted";
    }
}
