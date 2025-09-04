package com.example.eventregistration.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventReqDTO {
    private String name;
    private LocalDate date;
    private String location;
    private String description;
}
