package com.example.eventregistration.controller;

import com.example.eventregistration.entity.Event;
import com.example.eventregistration.entity.Registration;
import com.example.eventregistration.entity.User;
import com.example.eventregistration.repository.EventRepository;
import com.example.eventregistration.repository.UserRepository;
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
    public ResponseEntity<Registration> registerForEvent(@RequestParam Long eventId, Authentication authentication) {
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        Event event = eventService.findById(eventId);

        return registrationService.registerForEvent(user, event);
    }

    @GetMapping
    public ResponseEntity<List<Registration>> getMyRegistrations(Authentication authentication) {
        String username = authentication.getName();

        User user = userService.findByUsername(username);
        return registrationService.getMyRegistrations(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelRegistration(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();

        Registration registration = registrationService.findById(id);

        if (!registration.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only cancel your own registrations");
        }

        return registrationService.cancelRegistration(registration);
    }
}
