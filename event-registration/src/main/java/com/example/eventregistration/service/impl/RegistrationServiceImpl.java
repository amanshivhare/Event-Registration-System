package com.example.eventregistration.service.impl;

import com.example.eventregistration.dto.response.RegistrationResDTO;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.User;
import com.example.eventregistration.repository.RegistrationRepository;
import com.example.eventregistration.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Override
    public RegistrationResDTO registerForEvent(User user, Event event) {
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        return new RegistrationResDTO(registrationRepository.save(registration));
    }

    @Override
    public List<RegistrationResDTO> getMyRegistrations(User user) {
        return registrationRepository.findByUser(user)
                .stream()
                .map(RegistrationResDTO::new)
                .toList();
    }

    @Override
    public Registration findById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    @Override
    public String cancelRegistration(Long id, String username) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if (!registration.getUser().getUsername().equals(username)) {
            return "You can only cancel your own registrations.";
        }

        registrationRepository.delete(registration);
        return "Registration cancelled successfully.";
    }
}
