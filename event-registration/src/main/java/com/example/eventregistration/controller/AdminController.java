package com.example.eventregistration.controller;

import com.example.eventregistration.dto.response.UserResDTO;
import com.example.eventregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserResDTO> getAllUsers() {
        return userService.findAll()
                .stream()
                .map(UserResDTO::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
