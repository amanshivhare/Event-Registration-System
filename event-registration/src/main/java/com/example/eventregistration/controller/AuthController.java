package com.example.eventregistration.controller;

import com.example.eventregistration.dto.request.LoginReqDTO;
import com.example.eventregistration.dto.request.RegisterUserReqDTO;
import com.example.eventregistration.dto.response.LoginResDTO;
import com.example.eventregistration.dto.response.UserResDTO;
import com.example.eventregistration.service.impl.MyUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MyUserDetailsService myUserDetailsService;

    private final AuthenticationManager authenticationManager;

    public AuthController(MyUserDetailsService myUserDetailsService, AuthenticationManager authenticationManager) {
        this.myUserDetailsService = myUserDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResDTO> register(@RequestBody @Valid RegisterUserReqDTO registerUserReqDTO) {
        UserResDTO user = myUserDetailsService.registerUser(registerUserReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserResDTO> registerAdmin(@RequestBody @Valid RegisterUserReqDTO registerUserReqDTO) {
        UserResDTO admin = myUserDetailsService.registerAdmin(registerUserReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(admin);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@RequestBody @Valid LoginReqDTO loginReqDTO) {
        LoginResDTO response = myUserDetailsService.login(loginReqDTO, authenticationManager);
        return ResponseEntity.ok(response);
    }
}

