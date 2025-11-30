package com.game.leaderboardservice.service;

import com.game.leaderboardservice.dto.LeaderboardResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class LeaderboardService implements ILeaderboardService {

    private final StringRedisTemplate redisTemplate;
    private static final String LEADERBOARD_KEY = "leaderboard:global";

    public LeaderboardService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void updateScore(String username, long scores) {
        redisTemplate.opsForZSet().add(
                LEADERBOARD_KEY,
                username,
                scores
        );
    }

    public List<LeaderboardResponse> getTopN(int n) {
        Set<ZSetOperations.TypedTuple<String>> results =
                redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, n - 1);

        if (results == null) return List.of();

        List<LeaderboardResponse> list = new ArrayList<>();

        for (var tuple : results) {
            list.add(new LeaderboardResponse(
                    tuple.getValue(),
                    Objects.requireNonNull(tuple.getScore()).longValue()
            ));
        }

        return list;
    }
}

