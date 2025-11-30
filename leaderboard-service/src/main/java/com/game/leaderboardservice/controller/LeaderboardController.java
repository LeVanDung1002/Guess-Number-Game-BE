package com.game.leaderboardservice.controller;

import com.game.leaderboardservice.service.ILeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private ILeaderboardService iLeaderboardService;

    private final Integer QUANTITY = 10;

    @GetMapping
    public ResponseEntity<?> getLeaderboard() {
        try {
            return ResponseEntity.ok(iLeaderboardService.getTopN(QUANTITY));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
