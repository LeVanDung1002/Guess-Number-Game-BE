package com.game.gameservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GuessRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Number is required")
    @Min(1)
    @Max(5)
    private Integer numberGuess;
}