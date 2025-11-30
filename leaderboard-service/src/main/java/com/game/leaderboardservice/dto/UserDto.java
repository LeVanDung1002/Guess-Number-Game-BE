package com.game.leaderboardservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDto {
    private UUID userId;
    private String username;
    private Integer scores;
    private Integer turns;
}