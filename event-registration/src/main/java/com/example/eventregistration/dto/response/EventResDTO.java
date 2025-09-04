package com.example.eventregistration.dto.response;

import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResDTO {
    private Long id;
    private String name;
    private LocalDate date;
    private String location;
    private String description;
    private Set<Registration> registrations;

    public EventResDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.date = event.getDate();
        this.location = event.getLocation();
        this.description = event.getDescription();
        this.registrations = event.getRegistrations();
    }
}
