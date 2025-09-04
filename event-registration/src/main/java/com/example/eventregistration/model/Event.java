package com.example.eventregistration.model;

import com.example.eventregistration.dto.request.EventReqDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Registration> registrations;

    public Event(Long id, String name, LocalDate date, String location, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public Event(EventReqDTO eventReqDTO) {
        this.name = eventReqDTO.getName();
        this.date = eventReqDTO.getDate();
        this.location = eventReqDTO.getLocation();
        this.description = eventReqDTO.getDescription();
    }
}
