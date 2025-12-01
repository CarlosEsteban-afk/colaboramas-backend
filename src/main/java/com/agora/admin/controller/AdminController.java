package com.agora.admin.controller;

import com.agora.admin.model.AdminStats;
import com.agora.admin.model.CountryCount;
import com.agora.admin.service.AdminService;
import com.agora.event.model.Event;
import com.agora.user.model.User;
import com.agora.event.model.EventType;
import com.agora.auth.model.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @GetMapping("/usersNumber")
    public ResponseEntity<Long> getUsersNumber() {
        return ResponseEntity.ok(adminService.getUsersNumber());
    }

    @GetMapping("/usersPerRol")
    public ResponseEntity<java.util.Map<String, Long>> getUsersPerRol() {
        return ResponseEntity.ok(adminService.getUsersPerRole());
    }

    @GetMapping("/userPerCountry")
    public ResponseEntity<java.util.Map<String, Long>> getUsersPerCountry() {
        return ResponseEntity.ok(adminService.getUsersPerCountry());
    }

    @PatchMapping("/changeUserRole/{id}")
    public ResponseEntity<User> changeUserRole(@PathVariable Long id, @RequestParam("role") RoleEnum role) {
        return ResponseEntity.ok(adminService.changeUserRole(id, role));
    }

    // Events admin
    @GetMapping("/getEvents")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(adminService.getAllEvents());
    }

    @GetMapping("/getEvent/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getEventById(id));
    }

    @GetMapping("/eventsNumber")
    public ResponseEntity<Long> getEventsNumber() {
        return ResponseEntity.ok(adminService.getEventsNumber());
    }

    @GetMapping("/eventsPerType")
    public ResponseEntity<java.util.Map<String, Long>> getEventsPerType() {
        return ResponseEntity.ok(adminService.getEventsPerType());
    }

    @PatchMapping("/banEvent/{id}")
    public ResponseEntity<Event> banEvent(@PathVariable Long id, @RequestParam(name = "enabled", required = false) Boolean enabled) {
        return ResponseEntity.ok(adminService.banEvent(id, enabled));
    }

    @PatchMapping("/changeEventType/{id}")
    public ResponseEntity<Event> changeEventType(@PathVariable Long id, @RequestParam("type") EventType type) {
        return ResponseEntity.ok(adminService.changeEventType(id, type));
    }
}
