package com.game.gameservice.controller;

import com.game.gameservice.dto.GuessRequest;
import com.game.gameservice.dto.GuessResponse;
import com.game.gameservice.service.IGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final IGameService gameService;

    @PostMapping("/guess")
    public ResponseEntity<GuessResponse> guessNumber(@Valid @RequestBody GuessRequest request) {
        try {
            GuessResponse response = gameService.processGuess(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    GuessResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/buy-turns")
    public ResponseEntity<Integer> buyTurns(@RequestParam String userId) {
        try {
            Integer response = gameService.addTurns(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}