package com.example.eventregistration.service.impl;

import com.example.eventregistration.dto.request.EventReqDTO;
import com.example.eventregistration.dto.response.EventResDTO;
import com.example.eventregistration.exception.exceptions.ApiRequestException;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.repository.EventRepository;
import com.example.eventregistration.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ApiRequestException("Event not found with id: " + eventId, HttpStatus.NOT_FOUND));
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
                .orElseThrow(() -> new ApiRequestException("Event not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public EventResDTO create(EventReqDTO eventReqDTO) {
        Event event = new Event(eventReqDTO);
        return new EventResDTO(eventRepository.save(event));
    }

    @Override
    public EventResDTO update(Long id, EventReqDTO eventReqDTO) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Event not found with id: " + id, HttpStatus.NOT_FOUND));

        existing.setName(eventReqDTO.getName());
        existing.setDate(eventReqDTO.getDate());
        existing.setLocation(eventReqDTO.getLocation());
        existing.setDescription(eventReqDTO.getDescription());

        return new EventResDTO(eventRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ApiRequestException("Event not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        eventRepository.deleteById(id);
    }
}
