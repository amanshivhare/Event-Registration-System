package com.example.eventregistration.service;

import com.example.eventregistration.model.User;

public interface UserService {
    User findByUsername(String username);
}
