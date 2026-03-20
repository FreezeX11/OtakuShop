package com.Backend.Util;

import com.Backend.Entity.User;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthUtil {
    private final UserRepository userRepository;

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found with email: " + authentication.getName()));
    }
}
