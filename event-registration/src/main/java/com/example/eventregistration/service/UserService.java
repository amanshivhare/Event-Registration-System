package com.example.eventregistration.service;

import com.example.eventregistration.model.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    List<User> findAll();
    void deleteById(Long id);
}
