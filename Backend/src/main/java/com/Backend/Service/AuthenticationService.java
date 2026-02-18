package com.Backend.Service;

import com.Backend.Payload.Request.LoginRequest;
import com.Backend.Payload.Response.JwtResponse;
import com.Backend.SecurityConfig.CustomUserDetails;
import com.Backend.SecurityConfig.JWT.JwtUtils;
import com.Backend.ServiceInterface.IAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    @Override
    public JwtResponse signin(LoginRequest loginRequest) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                );

        Authentication authentication = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if(!userDetails.isEnabled()) {
            throw new DisabledException("User account is disable");
        }

        String jwtToken = jwtUtils.generateToken(userDetails.getUsername());
        String profile = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
                .getFirst();

        return new JwtResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                profile
        );
    }
}
