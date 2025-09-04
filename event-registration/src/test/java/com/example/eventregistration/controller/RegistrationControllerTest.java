package com.example.eventregistration.controller;

import com.example.eventregistration.config.AppConfig;
import com.example.eventregistration.config.SecurityConfig;
import com.example.eventregistration.entity.Authority;
import com.example.eventregistration.entity.Event;
import com.example.eventregistration.entity.Registration;
import com.example.eventregistration.entity.User;
import com.example.eventregistration.security.JwtUtil;
import com.example.eventregistration.security.MyUserDetailsService;
import com.example.eventregistration.service.EventService;
import com.example.eventregistration.service.RegistrationService;
import com.example.eventregistration.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@Import({AppConfig.class, SecurityConfig.class})
@AutoConfigureMockMvc(addFilters = true)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private MyUserDetailsService myUserDetailsService;

    @MockitoBean
    private RegistrationService registrationService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private EventService eventService;

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldRegisterForEvent() throws Exception {
        Set<Authority> authorities = Set.of(new Authority(1L, "ROLE_USER", null));
        User user = new User(1L, "test-user", "password", true, authorities);
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Registration registration = new Registration(1L, event, user, LocalDateTime.now());

        when(userService.findByUsername("test-user")).thenReturn(user);
        when(eventService.findById(1L)).thenReturn(event);
        when(registrationService.registerForEvent(any(User.class), any(Event.class)))
                .thenReturn(ResponseEntity.ok(registration));

        mockMvc.perform(post("/registrations")
                        .param("eventId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("test-user"));

    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldReturnMyRegistrations() throws Exception {
        Set<Authority> authorities = Set.of(new Authority(1L, "ROLE_USER", null));
        User user = new User(1L, "test-user", "password", true, authorities);
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Registration registration = new Registration(1L, event, user, LocalDateTime.now());

        when(userService.findByUsername("test-user")).thenReturn(user);
        when(registrationService.getMyRegistrations(user)).thenReturn(ResponseEntity.ok(List.of(registration)));

        mockMvc.perform(get("/registrations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.username").value("test-user"));
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldCancelRegistration() throws Exception {
        Set<Authority> authorities = Set.of(new Authority(1L, "ROLE_USER", null));
        User user = new User(1L, "test-user", "password", true, authorities);
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Registration registration = new Registration(1L, event, user, LocalDateTime.now());

        when(registrationService.findById(1L)).thenReturn(registration);
        when(registrationService.cancelRegistration(registration))
                .thenReturn(ResponseEntity.ok("Registration cancelled"));

        mockMvc.perform(delete("/registrations/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Registration cancelled"));
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldNotCancelOthersRegistration() throws Exception {
        Set<Authority> authorities = Set.of(new Authority(1L, "ROLE_USER", null));
        User otherUser = new User(2L, "john", "password", true, authorities);
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Registration registration = new Registration(1L, event, otherUser, LocalDateTime.now());

        when(registrationService.findById(1L)).thenReturn(registration);

        mockMvc.perform(delete("/registrations/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("You can only cancel your own registrations"));
    }
}
