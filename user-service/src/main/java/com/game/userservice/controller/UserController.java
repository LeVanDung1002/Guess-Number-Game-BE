package com.game.userservice.controller;

import com.game.userservice.dto.UserRequest;
import com.game.userservice.dto.UserResponse;
import com.game.userservice.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        try {
            UserResponse response = userService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserRequest request) {
        try {
            UserResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
