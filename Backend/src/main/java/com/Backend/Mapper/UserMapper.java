package com.Backend.Mapper;

import com.Backend.Entity.Profile;
import com.Backend.Entity.User;
import com.Backend.Enumeration.UserStatus;
import com.Backend.Payload.Request.SignupRequest;
import com.Backend.Payload.Response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User toUser(SignupRequest signupRequest, Profile profile) {
        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setUserProfile(profile);
        user.setCreatedDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        return user;
    }

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setCreationDate(user.getCreatedDate());
        userResponse.setEnable(user.getUserStatus() == UserStatus.ACTIVE);
        userResponse.setProfile(user.getUserProfile().getUserProfile().name());

        return userResponse;
    }

}
