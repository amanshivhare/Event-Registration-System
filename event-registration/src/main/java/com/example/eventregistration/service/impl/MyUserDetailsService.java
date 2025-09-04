package com.example.eventregistration.service.impl;

import com.example.eventregistration.dto.request.LoginReqDTO;
import com.example.eventregistration.dto.request.RegisterUserReqDTO;
import com.example.eventregistration.dto.response.LoginResDTO;
import com.example.eventregistration.dto.response.UserResDTO;
import com.example.eventregistration.model.Authority;
import com.example.eventregistration.model.User;
import com.example.eventregistration.repository.UserRepository;
import com.example.eventregistration.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities().stream()
                        .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                        .collect(Collectors.toList())
        );
    }

    public UserResDTO registerUser(RegisterUserReqDTO registerUserReqDTO) {
        registerUserReqDTO.setPassword(passwordEncoder.encode(registerUserReqDTO.getPassword()));

        User user = new User(registerUserReqDTO);

        Authority role = new Authority();
        role.setAuthority("ROLE_USER");
        role.setUser(user);

        user.setAuthorities(Set.of(role));

        User savedUser = userRepository.save(user);
        return new UserResDTO(savedUser);
    }

    public LoginResDTO login(@Valid LoginReqDTO loginReqDTO, AuthenticationManager authenticationManager) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReqDTO.getUsername(),
                        loginReqDTO.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginReqDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginReqDTO.getUsername()));

        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResDTO(new UserResDTO(user), token);
    }
}
