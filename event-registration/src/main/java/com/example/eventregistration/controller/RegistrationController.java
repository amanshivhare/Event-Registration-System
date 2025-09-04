package com.example.eventregistration.controller;

import com.example.eventregistration.dto.response.RegistrationResDTO;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.User;
import com.example.eventregistration.service.EventService;
import com.example.eventregistration.service.RegistrationService;
import com.example.eventregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<RegistrationResDTO> registerForEvent(@RequestParam Long eventId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Event event = eventService.findById(eventId);
        return new ResponseEntity<>(registrationService.registerForEvent(user, event), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RegistrationResDTO>> getMyRegistrations(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        return new ResponseEntity<>(registrationService.getMyRegistrations(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelRegistration(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        return new ResponseEntity<>(registrationService.cancelRegistration(id, username), HttpStatus.OK);
    }
}
