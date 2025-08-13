package com.tochratana.api.dto;

import lombok.Builder;

@Builder
public record RegisterResponse(
        String email,
        String firstName,
        String lastName
) {
}
