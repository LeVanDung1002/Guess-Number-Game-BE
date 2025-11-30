package com.game.leaderboardservice.service;

import com.game.leaderboardservice.dto.LeaderboardResponse;

import java.util.List;

public interface ILeaderboardService {
    void updateScore(String userId, long scores);

    List<LeaderboardResponse> getTopN(int n);
}
