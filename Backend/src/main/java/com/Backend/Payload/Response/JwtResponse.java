package com.Backend.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private Long id;

    private String email;

    private String profile;
}
