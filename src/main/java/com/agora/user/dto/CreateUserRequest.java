package com.agora.user.dto;


import com.agora.auth.model.RoleEnum;

import java.util.Set;

public record CreateUserRequest(
        String name,
        String username,
        String password,
        Set<RoleEnum> roles
) {}
