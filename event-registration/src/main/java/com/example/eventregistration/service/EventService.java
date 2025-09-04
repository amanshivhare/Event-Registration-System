package com.example.eventregistration.service;

import com.example.eventregistration.dto.request.EventReqDTO;
import com.example.eventregistration.dto.response.EventResDTO;
import com.example.eventregistration.model.Event;

import java.util.List;

public interface EventService {
    Event findById(Long eventId);
    List<EventResDTO> getAll();
    EventResDTO getById(Long id);
    EventResDTO create(EventReqDTO eventReqDTO);
    EventResDTO update(Long id, EventReqDTO eventReqDTO);
    void delete(Long id);
}
