package com.game.leaderboardservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreUpdateEvent {
    private String username;
    private Integer score;
}