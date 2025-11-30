package com.game.userservice.service;

import com.game.userservice.dto.UserRequest;
import com.game.userservice.dto.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse register(UserRequest user);

    UserResponse login(UserRequest user);

    UserResponse updateUser(UserRequest user);

    UserResponse get(String userId);

    List<UserResponse> getLeaderboardTop10();

    List<UserResponse> getUsers(List<String> uuids);

    UserResponse addTurn(String userId);
}
