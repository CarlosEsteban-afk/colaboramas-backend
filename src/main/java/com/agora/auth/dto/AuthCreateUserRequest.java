package com.agora.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;

public record AuthCreateUserRequest(
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String password,
        @Valid AuthCreateRoleRequest roleRequest
) {}
