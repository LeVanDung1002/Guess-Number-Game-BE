package com.game.gameservice.service;

import com.game.gameservice.client.UserServiceClient;
import com.game.gameservice.dto.GuessRequest;
import com.game.gameservice.dto.GuessResponse;
import com.game.gameservice.dto.UserDto;
import com.game.gameservice.event.ScoreUpdateEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService implements IGameService {

    private final Random random = new Random();
    private final KafkaTemplate<@NonNull String, @NonNull ScoreUpdateEvent> kafkaTemplate;
    private final UserServiceClient userServiceClient;

    @Transactional
    public GuessResponse processGuess(GuessRequest guessRequest) {

        UUID userId = UUID.fromString(guessRequest.getUserId());
        Integer guessNumber = guessRequest.getNumberGuess();

        UserDto user = userServiceClient.getUserById(userId);


        if (user.getTurns() <= 0) {
            throw new RuntimeException("No turns left. Please buy more turns.");
        }

        int serverNumber = random.nextInt(5) + 1;

        boolean isCorrect = guessNumber.equals(serverNumber);
        boolean forceWin = random.nextDouble() < 0.05;
        final boolean finalSuccess = forceWin || isCorrect;

        // 4. Tính toán kết quả
        int newScore = finalSuccess ? user.getScores() + 1 : user.getScores();
        int newTurns = user.getTurns() - 1;

        UserDto updatedUser = updateUserInUserService(userId, newScore, newTurns);

        if (finalSuccess) {
            sendScoreUpdateEvent(updatedUser);
        }

        // 5. Tạo response message
        String message = finalSuccess
                ? "🎉 Chính xác! Bạn đã đoán đúng! +1 điểm"
                : String.format("❌ Sai rồi! Số đúng là %d. Thử lại nhé!", serverNumber);

        if (forceWin && !isCorrect) {
            message = "🎁 May mắn! Bạn được 5% tỷ lệ thẳng! +1 điểm";
        }

        // 6. Tạo và trả về response
        return new GuessResponse(
                finalSuccess,
                message,
                serverNumber,
                guessNumber,
                new GuessResponse.UserDto(
                        userId.toString(),
                        updatedUser.getUsername(),
                        newScore,
                        newTurns
                )
        );
    }

    @Override
    public Integer addTurns(String userId) {
        UserDto userDto = userServiceClient.addTurns(userId);
        return userDto.getTurns();
    }

    private UserDto updateUserInUserService(UUID userId, Integer newScore, Integer newTurns) {
        return userServiceClient.updateUser(
                UserDto.builder()
                        .userId(userId)
                        .turns(newTurns)
                        .scores(newScore)
                        .build()
        );
    }

    private void sendScoreUpdateEvent(UserDto user) {
        try {
            ScoreUpdateEvent event = new ScoreUpdateEvent(
                    user.getUsername(),
                    user.getScores()
            );

            kafkaTemplate.send("score-updates", user.getUserId().toString(), event);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}