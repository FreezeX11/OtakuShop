package com.Backend.SecurityConfig;

import com.Backend.Payload.Response.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        ApiError apiError = new ApiError(
                "Authentication Failed",
                401,
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream()
                .println(objectMapper.writeValueAsString(apiError));
    }
}
