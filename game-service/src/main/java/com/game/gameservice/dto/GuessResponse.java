package com.game.gameservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuessResponse {
    private Boolean success;
    private String message;
    private Integer serverNumber;
    private Integer userGuess;
    private UserDto user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDto {
        private String id;
        private String username;
        private Integer score;
        private Integer turns;
    }
}