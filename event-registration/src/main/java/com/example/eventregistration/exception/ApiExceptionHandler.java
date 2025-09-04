package com.example.eventregistration.exception;

import com.example.eventregistration.dto.response.ErrorMsgDTO;
import com.example.eventregistration.exception.exceptions.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<ErrorMsgDTO> handleApiRequestException(ApiRequestException e) {
        ErrorMsgDTO errorMessageDTO = new ErrorMsgDTO(e.getMessage(), e.getStatus(), ZonedDateTime.now());
        return new ResponseEntity<>(errorMessageDTO, e.getStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMsgDTO> handleUsernameNotFoundException(UsernameNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorMsgDTO errorMessageDTO = new ErrorMsgDTO(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorMessageDTO, status);
    }

}
