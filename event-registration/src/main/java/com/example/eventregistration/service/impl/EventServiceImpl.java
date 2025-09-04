package com.example.eventregistration.service.impl;

import com.example.eventregistration.dto.request.EventReqDTO;
import com.example.eventregistration.dto.response.EventResDTO;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.repository.EventRepository;
import com.example.eventregistration.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public List<EventResDTO> getAll() {
        return eventRepository.findAll()
                .stream()
                .map(EventResDTO::new)
                .toList();
    }

    @Override
    public EventResDTO getById(Long id) {
        return eventRepository.findById(id)
                .map(EventResDTO::new)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }

    @Override
    public EventResDTO create(@RequestBody EventReqDTO eventReqDTO) {
        Event event = new Event(eventReqDTO);
        return new EventResDTO(eventRepository.save(event));
    }

    @Override
    public EventResDTO update(@PathVariable Long id, @RequestBody EventReqDTO eventReqDTO) {
        Event existing = eventRepository.findById(id).orElseThrow();
        existing.setName(eventReqDTO.getName());
        existing.setDate(eventReqDTO.getDate());
        existing.setLocation(eventReqDTO.getLocation());
        existing.setDescription(eventReqDTO.getDescription());
        return new EventResDTO(eventRepository.save(existing));
    }

    @Override
    public String delete(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "Event Deleted Successfully!";
    }
}
