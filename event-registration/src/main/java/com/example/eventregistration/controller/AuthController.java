package com.example.eventregistration.controller;

import com.example.eventregistration.dto.request.LoginReqDTO;
import com.example.eventregistration.dto.request.RegisterUserReqDTO;
import com.example.eventregistration.dto.response.LoginResDTO;
import com.example.eventregistration.dto.response.UserResDTO;
import com.example.eventregistration.service.impl.MyUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<UserResDTO> register(@RequestBody @Valid RegisterUserReqDTO registerUserReqDTO) {
        return new ResponseEntity<>(myUserDetailsService.registerUser(registerUserReqDTO), HttpStatus.OK);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserResDTO> registerAdmin(@RequestBody @Valid RegisterUserReqDTO registerUserReqDTO) {
        return new ResponseEntity<>(myUserDetailsService.registerAdmin(registerUserReqDTO), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@RequestBody @Valid LoginReqDTO loginReqDTO) {
        return new ResponseEntity<>(myUserDetailsService.login(loginReqDTO, authenticationManager), HttpStatus.OK);
    }
}
