package com.example.eventregistration.controller;

import com.example.eventregistration.config.AppConfig;
import com.example.eventregistration.config.SecurityConfig;
import com.example.eventregistration.dto.response.RegistrationResDTO;
import com.example.eventregistration.exception.exceptions.ApiRequestException;
import com.example.eventregistration.model.Authority;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.User;
import com.example.eventregistration.security.JwtUtil;
import com.example.eventregistration.service.impl.MyUserDetailsService;
import com.example.eventregistration.service.impl.EventServiceImpl;
import com.example.eventregistration.service.impl.RegistrationServiceImpl;
import com.example.eventregistration.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    private RegistrationServiceImpl registrationServiceImpl;

    @MockitoBean
    private UserServiceImpl userServiceImpl;

    @MockitoBean
    private EventServiceImpl eventServiceImpl;

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldRegisterForEvent() throws Exception {
        Set<Authority> authorities = Set.of(new Authority(1L, "ROLE_USER", null));
        User user = new User(1L, "test-user", "password", true, authorities);
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");

        RegistrationResDTO registrationResDTO = new RegistrationResDTO(
                1L,
                event,
                user,
                LocalDateTime.now()
        );

        when(userServiceImpl.findByUsername("test-user")).thenReturn(user);
        when(eventServiceImpl.findById(1L)).thenReturn(event);
        when(registrationServiceImpl.registerForEvent(any(User.class), any(Event.class)))
                .thenReturn(registrationResDTO);

        mockMvc.perform(post("/registrations")
                        .param("eventId", "1")
                        .principal(() -> "test-user"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.username").value("test-user"))
                .andExpect(jsonPath("$.event.id").value(1));
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldReturnMyRegistrations() throws Exception {
        Set<Authority> authorities = Set.of(new Authority(1L, "ROLE_USER", null));
        User user = new User(1L, "test-user", "password", true, authorities);
        Event event = new Event(1L, "Hackathon", LocalDate.now(), "Berlin", "Tech event");
        Registration registration = new Registration(1L, event, user, LocalDateTime.now());

        when(userServiceImpl.findByUsername("test-user")).thenReturn(user);
        List<RegistrationResDTO> list = List.of(registration)
                .stream()
                .map(RegistrationResDTO::new)
                .toList();

        when(registrationServiceImpl.getMyRegistrations(user)).thenReturn(list);

        mockMvc.perform(get("/registrations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.username").value("test-user"));
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldCancelRegistration() throws Exception {
        doNothing().when(registrationServiceImpl).cancelRegistration(1L, "test-user");

        mockMvc.perform(delete("/registrations/1"))
                .andExpect(status().isNoContent())   // 204
                .andExpect(content().string(""));    // empty body
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldNotCancelOthersRegistration() throws Exception {
        doThrow(new ApiRequestException("You can only cancel your own registrations.", HttpStatus.FORBIDDEN))
                .when(registrationServiceImpl).cancelRegistration(1L, "test-user");

        mockMvc.perform(delete("/registrations/1"))
                .andExpect(status().isForbidden())   // 403
                .andExpect(jsonPath("$.message").value("You can only cancel your own registrations.")); // error handled by ApiExceptionHandler
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldReturnNotFoundWhenRegistrationDoesNotExist() throws Exception {
        doThrow(new ApiRequestException("Registration not found with id 1", HttpStatus.NOT_FOUND))
                .when(registrationServiceImpl).cancelRegistration(1L, "test-user");

        mockMvc.perform(delete("/registrations/1"))
                .andExpect(status().isNotFound())   // 404
                .andExpect(jsonPath("$.message").value("Registration not found with id 1"));
    }
}
