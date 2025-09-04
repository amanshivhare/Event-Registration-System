package com.example.eventregistration.service.impl;

import com.example.eventregistration.dto.response.RegistrationResDTO;
import com.example.eventregistration.exception.exceptions.ApiRequestException;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.User;
import com.example.eventregistration.repository.RegistrationRepository;
import com.example.eventregistration.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() ->
                        new ApiRequestException("Registration not found with id " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public void cancelRegistration(Long id, String username) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() ->
                        new ApiRequestException("Registration not found with id " + id, HttpStatus.NOT_FOUND));

        if (!registration.getUser().getUsername().equals(username)) {
            throw new ApiRequestException("You can only cancel your own registrations.", HttpStatus.FORBIDDEN);
        }

        registrationRepository.delete(registration);
    }
}
