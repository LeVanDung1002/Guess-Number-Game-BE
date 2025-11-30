package com.game.leaderboardservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.leaderboardservice.event.ScoreUpdateEvent;
import com.game.leaderboardservice.service.ILeaderboardService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ScoreUpdatedListener {

    private final ILeaderboardService leaderboardService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScoreUpdatedListener(ILeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @KafkaListener(topics = "score-updates")
    public void consume(String message) throws Exception {
        ScoreUpdateEvent event = objectMapper.readValue(message, ScoreUpdateEvent.class);
        leaderboardService.updateScore(event.getUsername(), event.getScore());
    }
}

