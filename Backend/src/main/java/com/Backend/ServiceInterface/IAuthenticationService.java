package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.LoginRequest;
import com.Backend.Payload.Response.JwtResponse;

public interface IAuthenticationService {
    JwtResponse signin(LoginRequest loginRequest);
}
