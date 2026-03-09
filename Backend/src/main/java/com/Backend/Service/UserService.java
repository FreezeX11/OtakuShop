package com.Backend.Service;

import com.Backend.Entity.Profile;
import com.Backend.Entity.User;
import com.Backend.Enumeration.UserProfile;
import com.Backend.Enumeration.UserStatus;
import com.Backend.Mapper.UserMapper;
import com.Backend.Payload.Request.SignupRequest;
import com.Backend.Repository.ProfileRepository;
import com.Backend.Repository.UserRepository;
import com.Backend.ServiceInterface.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserMapper userMapper;

    @Override
    public void registerUser(SignupRequest signupRequest) {
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(user -> {
            throw new RuntimeException("Email already exist!");
        });

        Profile profile = profileRepository.findByUserProfile(
                UserProfile.valueOf(signupRequest.getProfile())
                )
                .orElseThrow(() ->
                        new RuntimeException(signupRequest.getProfile() + " not found")
                );

        userRepository.save(userMapper.toUser(signupRequest, profile));
    }

    @Override
    public void updateUser(Long id, SignupRequest signupRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id: " + id +"not found"));

        existingUser.setEmail(signupRequest.getEmail());
        existingUser.setLastModifiedDate(LocalDateTime.now());

        Profile profile = profileRepository.findByUserProfile(
                        UserProfile.valueOf(signupRequest.getProfile())
                )
                .orElseThrow(() ->
                        new RuntimeException(signupRequest.getProfile() + "not found")
                );

        existingUser.setUserProfile(profile);

        userRepository.save(existingUser);
    }

    @Override
    public void activateUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This user doesn't exist"));

        existingUser.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(existingUser);
    }

    @Override
    public void deactivateUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This user doesn't exist"));

        existingUser.setUserStatus(UserStatus.INACTIVE);
        userRepository.save(existingUser);
    }
}
