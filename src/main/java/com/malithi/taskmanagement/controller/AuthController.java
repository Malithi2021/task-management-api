package com.malithi.taskmanagement.controller;

import com.malithi.taskmanagement.dto.UserDto;
import com.malithi.taskmanagement.model.User;
import com.malithi.taskmanagement.security.AuthRequest;
import com.malithi.taskmanagement.security.AuthResponse;
import com.malithi.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        // Add default role if not specified
        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            Set<String> roles = new HashSet<>();
            roles.add("USER");
            userDto.setRoles(roles);
        }

        return ResponseEntity.ok(userService.createUser(userDto));
    }
}