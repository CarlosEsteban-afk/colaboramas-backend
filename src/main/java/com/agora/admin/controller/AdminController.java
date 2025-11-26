package com.agora.admin.controller;

import com.agora.admin.model.AdminStats;
import com.agora.admin.model.CountryCount;
import com.agora.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/refresh-stats")
    public ResponseEntity<AdminStats> refreshStats() {
        AdminStats stats = adminService.refreshStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStats> getLatestStats() {
        AdminStats stats = adminService.getLatestStats();
        if (stats == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<CountryCount>> getTopCountries() {
        return ResponseEntity.ok(adminService.getTopCountries());
    }
}
