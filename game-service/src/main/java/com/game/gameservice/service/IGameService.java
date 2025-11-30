package com.game.gameservice.service;

import com.game.gameservice.dto.GuessRequest;
import com.game.gameservice.dto.GuessResponse;

public interface IGameService {
    GuessResponse processGuess(GuessRequest guessRequest);

    Integer addTurns(String userId);
}
