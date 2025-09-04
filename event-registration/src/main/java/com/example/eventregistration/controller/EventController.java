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
        return new ResponseEntity<>(eventService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(eventService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventResDTO> create(@RequestBody EventReqDTO eventReqDTO) {
        return new ResponseEntity<>(eventService.create(eventReqDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResDTO> update(@PathVariable Long id, @RequestBody EventReqDTO eventReqDTO) {
        return new ResponseEntity<>(eventService.update(id, eventReqDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(eventService.delete(id), HttpStatus.OK);
    }
}
