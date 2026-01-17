package com.example.tp1_framework.dto;

import com.example.tp1_framework.model.Role;

public record UserResponse(
        Long id,
        String username,
        Role role
) {}
