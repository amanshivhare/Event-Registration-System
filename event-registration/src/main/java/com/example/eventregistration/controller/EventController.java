package com.example.eventregistration.controller;

import com.example.eventregistration.dto.request.EventReqDTO;
import com.example.eventregistration.dto.response.EventResDTO;
import com.example.eventregistration.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResDTO>> getAll() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EventResDTO> create(@RequestBody EventReqDTO eventReqDTO) {
        EventResDTO createdEvent = eventService.create(eventReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResDTO> update(@PathVariable Long id, @RequestBody EventReqDTO eventReqDTO) {
        return ResponseEntity.ok(eventService.update(id, eventReqDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}