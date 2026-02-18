package com.Backend.Controller;

import com.Backend.Payload.Request.LoginRequest;
import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Payload.Response.JwtResponse;
import com.Backend.Service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    ResponseEntity<ApiResponse> signin(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authenticationService.signin(loginRequest);

        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                jwtResponse
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }
}
