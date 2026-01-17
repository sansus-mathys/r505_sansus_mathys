package com.example.tp1_framework.dto;

import com.example.tp1_framework.model.Role;

public record UserCreateRequest(
        String username,
        String password,
        Role role
) {}
