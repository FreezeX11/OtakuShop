package com.Backend.Payload.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    @NotBlank(message = "Email shouldn't be null")
    @Email
    private String email;

    @Size(min = 8, max = 64, message = "Password must be between 2 and 50 characters")
    private String password;

    @NotBlank(message = "Profile shouldn't be null")
    private String profile;
}
