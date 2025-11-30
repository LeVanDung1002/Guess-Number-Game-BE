package com.game.userservice.service;

import com.game.userservice.dto.UserRequest;
import com.game.userservice.dto.UserResponse;

public interface IUserService {
    public UserResponse register(UserRequest user);
    public UserResponse login(UserRequest user);
}
