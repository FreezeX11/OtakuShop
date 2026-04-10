package com.Backend.Service;

import com.Backend.Entity.Profile;
import com.Backend.Entity.User;
import com.Backend.Enumeration.UserProfile;
import com.Backend.Enumeration.UserStatus;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.UserMapper;
import com.Backend.Payload.Request.SignupRequest;
import com.Backend.Payload.Request.UserRequest;
import com.Backend.Repository.ProfileRepository;
import com.Backend.Repository.UserRepository;
import com.Backend.ServiceInterface.IUserService;
import com.Backend.Util.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserMapper userMapper;
    private final UserUtil userUtil;

    @Transactional
    @Override
    public void registerUser(SignupRequest signupRequest) {
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(user -> {
            throw new ResourceAlreadyExistException("Email already exist!");
        });

        Profile profile = profileRepository.findByUserProfile(UserProfile.USER)
                .orElseThrow(() -> new ResourceNotFoundException(UserProfile.USER + " not found"));

        User user = userRepository.save(
                userMapper.toUser(
                        signupRequest.getEmail(),
                        signupRequest.getPassword(),
                        profile
                )
        );
        userUtil.createCartIfEligible(user);
    }

    @Override
    public void createUser(UserRequest userRequest) {
        userRepository.findByEmail(userRequest.getEmail())
                .ifPresent(user -> {
                    throw new ResourceAlreadyExistException("Email already exist!");
                });

        Profile profile = profileRepository.findByUserProfile(UserProfile.valueOf(userRequest.getProfile()))
                .orElseThrow(() -> new ResourceNotFoundException(userRequest.getProfile() + " not found"));

        userRepository.save(
                userMapper.toUser(
                        userRequest.getEmail(),
                        userRequest.getPassword(),
                        profile
                ));
    }

    @Override
    public void updateUser(Long id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id +"not found"));

        Profile profile = profileRepository.findByUserProfile(UserProfile.valueOf(userRequest.getProfile()))
                .orElseThrow(() -> new ResourceNotFoundException(userRequest.getProfile() + " not found"));

        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPassword(userRequest.getPassword());
        existingUser.setUserProfile(profile);
        existingUser.setLastModifiedDate(LocalDateTime.now());

        userRepository.save(existingUser);
    }

    @Override
    public void activateUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This user doesn't exist"));

        existingUser.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(existingUser);
    }

    @Override
    public void deactivateUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This user doesn't exist"));

        existingUser.setUserStatus(UserStatus.INACTIVE);
        userRepository.save(existingUser);
    }
}
