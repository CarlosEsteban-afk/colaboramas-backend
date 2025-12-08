package com.agora.admin.dto;

import com.agora.auth.model.RoleEnum;

public class ChangeRoleRequest {
    private RoleEnum role;

    public ChangeRoleRequest() {
    }

    public ChangeRoleRequest(RoleEnum role) {
        this.role = role;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
