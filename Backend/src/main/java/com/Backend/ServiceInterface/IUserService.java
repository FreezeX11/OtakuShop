package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.SignupRequest;
import com.Backend.Payload.Request.UpdateUserRequest;
import com.Backend.Payload.Request.UserRequest;

public interface IUserService {
    void registerUser(SignupRequest signupRequest);
    void createUser(UserRequest userRequest);
    void updateUser(Long id, UserRequest userRequest);
    void enableUser(Long id);
    void disableUser(Long id);
}
