package com.agora.admin.controller;

import com.agora.admin.service.AdminService;
import com.agora.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminApiController {

    @Autowired
    private AdminService adminService;

    @PatchMapping("/api/banUser/{id}")
    public ResponseEntity<User> banUser(@PathVariable Long id, @RequestParam(name = "enabled", required = false) Boolean enabled) {
        User u = adminService.banUser(id, enabled);
        return ResponseEntity.ok(u);
    }
}
