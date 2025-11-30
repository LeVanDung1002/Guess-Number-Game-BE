package com.game.userservice.service;

import com.game.userservice.dto.UserRequest;
import com.game.userservice.dto.UserResponse;
import com.game.userservice.model.User;
import com.game.userservice.repository.UserRepository;
import com.game.userservice.util.JWTUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        String token = jwtUtils.generateToken(savedUser.getUsername(), savedUser.getId());

        return UserResponse.builder().token(token).username(savedUser.getUsername()).turn(savedUser.getTurns()).score(savedUser.getScore()).build();
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

        return UserResponse.builder().token(token).username(user.getUsername()).turn(user.getTurns()).score(user.getScore()).build();
    }
}
