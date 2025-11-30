package com.game.userservice.service;

import com.game.userservice.dto.UserRequest;
import com.game.userservice.dto.UserResponse;
import com.game.userservice.event.ScoreUpdateEvent;
import com.game.userservice.model.User;
import com.game.userservice.repository.UserRepository;
import com.game.userservice.util.JWTUtils;
import lombok.NonNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<@NonNull String, @NonNull ScoreUpdateEvent> kafkaTemplate;
    private final JWTUtils jwtUtils;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       KafkaTemplate<@NonNull String, @NonNull ScoreUpdateEvent> kafkaTemplate, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
        this.jwtUtils = jwtUtils;
    }

    public UserResponse register(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder().username(username).password(passwordEncoder.encode(password)).build();

        User savedUser = userRepository.save(user);

        sendScoreUpdateEvent(savedUser);

        String token = jwtUtils.generateToken(savedUser.getUsername(), savedUser.getId());

        return UserResponse.builder()
                .token(token)
                .userId(String.valueOf(user.getId()))
                .username(savedUser.getUsername())
                .turns(savedUser.getTurns())
                .scores(savedUser.getScore())
                .build();
    }

    public UserResponse login(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtils.generateToken(user.getUsername(), user.getId());

        return UserResponse.builder()
                .token(token)
                .userId(String.valueOf(user.getId()))
                .username(user.getUsername())
                .turns(user.getTurns())
                .scores(user.getScore())
                .build();
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        Optional<User> user = userRepository.findById(UUID.fromString(userRequest.getUserId()));

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User userGet = user.get();

        userGet.setScore(userRequest.getScores());
        userGet.setTurns(userRequest.getTurns());

        User savedUser = userRepository.save(userGet);

        return UserResponse.builder()
                .userId(String.valueOf(savedUser.getId()))
                .scores(savedUser.getScore())
                .turns(savedUser.getTurns())
                .username(savedUser.getUsername())
                .build();
    }

    @Override
    public UserResponse get(String userId) {

        UUID uuid = UUID.fromString(userId);

        Optional<User> user = userRepository.findById(uuid);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User userInfo = user.get();

        return UserResponse.builder()
                .token("")
                .userId(String.valueOf(userInfo.getId()))
                .username(userInfo.getUsername())
                .turns(userInfo.getTurns())
                .scores(userInfo.getScore())
                .build();
    }

    @Override
    public List<UserResponse> getLeaderboardTop10() {
        List<User> users = userRepository.findTop10ByOrderByScoreDesc();

        return users.stream().map(user -> UserResponse.builder()
                .username(user.getUsername())
                .scores(user.getScore())
                .userId(String.valueOf(user.getId()))
                .turns(user.getTurns())
                .build()).toList();
    }

    @Override
    public List<UserResponse> getUsers(List<String> uuids) {
        List<UUID> userUUIDs = uuids.stream().map(UUID::fromString).toList();
        List<User> users = userRepository.findAllById(userUUIDs);

        return users.stream().map(user ->
                        UserResponse.builder()
                                .userId(String.valueOf(user.getId()))
                                .username(user.getUsername())
                                .build())
                .toList();
    }

    @Override
    public UserResponse addTurn(String userId) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        user.get().setTurns(user.get().getTurns() + 5);
        User userSaved = userRepository.save(user.get());

        return UserResponse.builder()
                .userId(String.valueOf(userSaved.getId()))
                .username(userSaved.getUsername())
                .turns(userSaved.getTurns())
                .scores(userSaved.getScore())
                .build();
    }

    private void sendScoreUpdateEvent(User user) {
        try {
            ScoreUpdateEvent event = new ScoreUpdateEvent(
                    user.getUsername(),
                    user.getScore()
            );

            kafkaTemplate.send("score-updates", user.getId().toString(), event);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
