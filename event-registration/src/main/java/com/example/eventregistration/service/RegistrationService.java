package com.example.eventregistration.service;

import com.example.eventregistration.entity.Event;
import com.example.eventregistration.entity.Registration;
import com.example.eventregistration.entity.User;
import com.example.eventregistration.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    public ResponseEntity<Registration> registerForEvent(User user, Event event) {
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);

        return ResponseEntity.ok().body(registrationRepository.save(registration));
    }

    public ResponseEntity<List<Registration>> getMyRegistrations(User user) {
        List<Registration> registrations = registrationRepository.findByUser(user);
        return ResponseEntity.ok(registrations);
    }

    public Registration findById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    public ResponseEntity<String> cancelRegistration(Registration registration) {
        registrationRepository.delete(registration);
        return ResponseEntity.ok("Registration cancelled");
    }
}
