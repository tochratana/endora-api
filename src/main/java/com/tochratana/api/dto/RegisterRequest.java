package com.tochratana.api.dto;

public record RegisterRequest(
        String username,
        String email,
        String firstName,
        String lastName,
        String password,
        String confirmedPassword
) {
}
