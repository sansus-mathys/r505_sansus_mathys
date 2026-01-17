package com.example.tp1_framework.service;

import com.example.tp1_framework.dto.UserCreateRequest;
import com.example.tp1_framework.dto.UserResponse;
import com.example.tp1_framework.exception.BadRequestException;
import com.example.tp1_framework.exception.NotFoundException;
import com.example.tp1_framework.model.User;
import com.example.tp1_framework.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public UserResponse create(UserCreateRequest req) {
        if (req.username() == null || req.username().isBlank()) {
            throw new BadRequestException("username is required");
        }
        if (req.password() == null || req.password().isBlank()) {
            throw new BadRequestException("password is required");
        }
        if (req.role() == null) {
            throw new BadRequestException("role is required");
        }
        users.findByUsername(req.username()).ifPresent(u -> {
            throw new BadRequestException("username already exists");
        });

        User saved = users.save(new User(req.username(), req.password(), req.role()));
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getRole());
    }

    public User getEntityById(Long id) {
        return users.findById(id).orElseThrow(() -> new NotFoundException("User " + id + " not found"));
    }
}
