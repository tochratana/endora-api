package com.tochratana.api.service;


import com.tochratana.api.dto.RegisterRequest;
import com.tochratana.api.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);
    void verify(String userId);
}
