package com.agora.user.dto;


import com.agora.user.model.RoleEnum;

import java.util.Set;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        Set<RoleEnum> roles
) {}
