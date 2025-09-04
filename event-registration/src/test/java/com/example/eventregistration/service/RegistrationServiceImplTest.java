package com.example.eventregistration.service;

import com.example.eventregistration.dto.response.RegistrationResDTO;
import com.example.eventregistration.model.Authority;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.User;
import com.example.eventregistration.repository.RegistrationRepository;
import com.example.eventregistration.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceImplTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationServiceImpl;

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
        Registration registration = new Registration(1L, event, user, LocalDateTime.now());
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);
        RegistrationResDTO response = registrationServiceImpl.registerForEvent(user, event);
        assertNotNull(response);
        assertNotNull(response);
        assertEquals(user.getUsername(), response.getUser().getUsername());
        assertEquals(event.getId(), response.getEvent().getId());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testGetMyRegistrations() {
        when(registrationRepository.findByUser(user)).thenReturn(List.of(registration));
        List<RegistrationResDTO> response = registrationServiceImpl.getMyRegistrations(user);
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(registrationRepository, times(1)).findByUser(user);
    }

    @Test
    void testFindByIdFound() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        Registration found = registrationServiceImpl.findById(1L);
        assertNotNull(found);
        assertEquals(registration, found);
        verify(registrationRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(registrationRepository.findById(2L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> registrationServiceImpl.findById(2L));
        assertEquals("Registration not found", exception.getMessage());
        verify(registrationRepository, times(1)).findById(2L);
    }

    @Test
    void testCancelRegistration() {
        Set<Authority> authorities = Set.of(new Authority(1L, "ROLE_USER", null));
        User user = new User(1L, "test-user", "password", true, authorities);
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Registration registration = new Registration(1L, event, user, LocalDateTime.now());
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        String result = registrationServiceImpl.cancelRegistration(1L, "test-user");
        assertEquals("Registration cancelled successfully.", result);
        verify(registrationRepository, times(1)).delete(registration);
    }

}
