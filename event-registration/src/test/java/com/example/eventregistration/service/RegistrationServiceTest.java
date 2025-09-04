package com.example.eventregistration.service;

import com.example.eventregistration.entity.Event;
import com.example.eventregistration.entity.Registration;
import com.example.eventregistration.entity.User;
import com.example.eventregistration.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationService registrationService;

    private User user;
    private Event event;
    private Registration registration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("test-user");

        event = new Event();
        event.setId(1L);
        event.setName("Hackathon");
        event.setDate(LocalDate.now());

        registration = new Registration();
        registration.setId(1L);
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDateTime.now());
    }

    @Test
    void testRegisterForEvent() {
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        ResponseEntity<Registration> response = registrationService.registerForEvent(user, event);

        assertNotNull(response);
        assertEquals(registration, response.getBody());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testGetMyRegistrations() {
        when(registrationRepository.findByUser(user)).thenReturn(List.of(registration));

        ResponseEntity<List<Registration>> response = registrationService.getMyRegistrations(user);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals(registration, response.getBody().get(0));
        verify(registrationRepository, times(1)).findByUser(user);
    }

    @Test
    void testFindByIdFound() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        Registration found = registrationService.findById(1L);

        assertNotNull(found);
        assertEquals(registration, found);
        verify(registrationRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(registrationRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> registrationService.findById(2L));
        assertEquals("Registration not found", exception.getMessage());
        verify(registrationRepository, times(1)).findById(2L);
    }

    @Test
    void testCancelRegistration() {
        doNothing().when(registrationRepository).delete(registration);

        ResponseEntity<String> response = registrationService.cancelRegistration(registration);

        assertNotNull(response);
        assertEquals("Registration cancelled", response.getBody());
        verify(registrationRepository, times(1)).delete(registration);
    }
}
