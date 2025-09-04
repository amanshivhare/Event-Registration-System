package com.example.eventregistration.service.impl;

import com.example.eventregistration.exception.exceptions.ApiRequestException;
import com.example.eventregistration.model.User;
import com.example.eventregistration.repository.UserRepository;
import com.example.eventregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ApiRequestException("User not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
