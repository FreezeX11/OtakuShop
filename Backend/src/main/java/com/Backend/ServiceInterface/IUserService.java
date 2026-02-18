package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.SignupRequest;

public interface IUserService {
    void registerUser(SignupRequest signupRequest);
    void updateUser(Long id, SignupRequest signupRequest);
    void activateUser(Long id);
    void deactivateUser(Long id);
}
