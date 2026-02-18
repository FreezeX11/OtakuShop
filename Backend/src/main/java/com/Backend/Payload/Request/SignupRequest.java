package com.Backend.Payload.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Email shouldn't be null")
    @Email
    private String email;

    @Size(min = 8, max = 64, message = "Password must be between 2 and 50 characters")
    private String password;

    private String profile;
}
