package com.example.eventregistration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMsgDTO {
    private String message;
    private HttpStatus httpStatus;
    private ZonedDateTime timestamp;
}
