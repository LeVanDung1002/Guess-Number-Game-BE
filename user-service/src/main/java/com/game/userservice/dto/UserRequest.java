package com.game.userservice.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String userId;
    private Integer turns;
    private Long scores;
    private String username;
    private String password;
}
