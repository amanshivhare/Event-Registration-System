package com.example.eventregistration.repository;

import com.example.eventregistration.entity.Event;
import com.example.eventregistration.entity.Registration;
import com.example.eventregistration.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RegistrationRepositoryTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Event event;
    private Registration registration;

    @BeforeEach
    void setUp() {
        // Create a user
        user = new User();
        user.setUsername("test-user");
        user.setPassword("password");
        user.setEnabled(true);
        user = entityManager.persistAndFlush(user); // Save user first

        // Create an event
        event = new Event();
        event.setName("Hackathon");
        event.setDate(LocalDate.now());
        event.setLocation("Berlin");
        event.setDescription("Tech event");
        event = entityManager.persistAndFlush(event); // Save event first

        // Create a registration
        registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDateTime.now());
    }

    @Test
    void testSaveAndFindById() {
        // Save registration
        Registration saved = registrationRepository.save(registration);

        // Fetch by ID
        Optional<Registration> fetched = registrationRepository.findById(saved.getId());

        assertTrue(fetched.isPresent());
        assertEquals(saved.getId(), fetched.get().getId());
        assertEquals("test-user", fetched.get().getUser().getUsername());
        assertEquals("Hackathon", fetched.get().getEvent().getName());
    }

    @Test
    void testFindByUser() {
        registrationRepository.save(registration);

        List<Registration> registrations = registrationRepository.findByUser(user);

        assertNotNull(registrations);
        assertEquals(1, registrations.size());
        assertEquals("Hackathon", registrations.get(0).getEvent().getName());
    }

    @Test
    void testDelete() {
        Registration saved = registrationRepository.save(registration);

        registrationRepository.delete(saved);

        Optional<Registration> deleted = registrationRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }
}
