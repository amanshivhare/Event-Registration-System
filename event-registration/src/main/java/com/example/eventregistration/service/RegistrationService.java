package com.example.eventregistration.service;

import com.example.eventregistration.dto.response.RegistrationResDTO;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.User;

import java.util.List;

public interface RegistrationService {
    RegistrationResDTO registerForEvent(User user, Event event);
    List<RegistrationResDTO> getMyRegistrations(User user);
    Registration findById(Long id);
    void cancelRegistration(Long id, String username);
}
