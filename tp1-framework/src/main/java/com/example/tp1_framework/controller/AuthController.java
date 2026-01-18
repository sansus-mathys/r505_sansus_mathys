package com.example.tp1_framework.controller;

import com.example.tp1_framework.dto.LoginRequest;
import com.example.tp1_framework.security.JwtDTO;
import com.example.tp1_framework.security.TokenGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;

    public AuthController(AuthenticationManager authenticationManager, TokenGenerator tokenGenerator) {
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping("/login")
    public JwtDTO login(@RequestBody LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        String token = tokenGenerator.generateJwtToken(authentication);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new JwtDTO(token, authentication.getName(), roles);
    }
}
