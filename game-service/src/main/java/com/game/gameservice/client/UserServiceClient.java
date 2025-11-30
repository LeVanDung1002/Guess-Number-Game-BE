package com.game.gameservice.client;

import com.game.gameservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/auth/me/{userId}")
    UserDto getUserById(@PathVariable("userId") UUID userId);

    @PatchMapping("/api/auth/update-user")
    UserDto updateUser(@RequestBody UserDto user);

    @PostMapping("/api/auth/add-turns")
    UserDto addTurns(@RequestParam String userId);
}