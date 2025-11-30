package com.game.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {
    private String userId;
    private String token;
    private String username;
    private Integer turns;
    private Long scores;
}
