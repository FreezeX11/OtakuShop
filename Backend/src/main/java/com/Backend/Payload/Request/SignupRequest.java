package com.Backend.Payload.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

//    @Size(min = 8, max = 8, message = "Phone number should contain 8 digits")
//    @Pattern(regexp = "\\d{8}", message = "The number should only contain digits")
//    private String phoneNumber;

    @Size(min = 8, max = 64, message = "Password must be between 2 and 50 characters")
    private String password;

    @NotBlank(message = "Profile shouldn't be null")
    private String profile;
}
