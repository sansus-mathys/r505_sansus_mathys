package com.example.tp1_framework.controller;

import com.example.tp1_framework.dto.UserCreateRequest;
import com.example.tp1_framework.dto.UserResponse;
import com.example.tp1_framework.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService users;

    public UserController(UserService users) {
        this.users = users;
    }

    @PostMapping
    public UserResponse create(@RequestBody UserCreateRequest req) {
        return users.create(req);
    }
}
