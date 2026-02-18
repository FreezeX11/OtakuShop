package com.Backend.Payload.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private Long id;

    private String email;

    private boolean enable;

    private String profile;

    private LocalDateTime creationDate;
}
