package com.example.eventregistration.dto.response;

import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResDTO {
    private Long id;
    private Event event;
    private User user;
    private LocalDateTime registrationDate;

    public RegistrationResDTO(Registration registration) {
        this.id = registration.getId();
        this.event = registration.getEvent();
        this.user = registration.getUser();
        this.registrationDate = registration.getRegistrationDate();
    }

    public RegistrationResDTO(long id, Event event, User user, LocalDateTime now) {
        this.id = id;
        this.event = event;
        this.user = user;
        this.registrationDate = now;
    }
}
