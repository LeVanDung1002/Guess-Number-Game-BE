package com.game.userservice.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
}
