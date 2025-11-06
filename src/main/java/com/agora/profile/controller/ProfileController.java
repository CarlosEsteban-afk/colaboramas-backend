package com.agora.profile.controller;

import com.agora.profile.dto.UpdateProfileRequest;
import com.agora.profile.dto.UserProfileResponseDto;
import com.agora.profile.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final IProfileService profileService;

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> updateProfile(@PathVariable Long userId, @RequestBody UpdateProfileRequest request) {
        UserProfileResponseDto updatedUser = profileService.updateProfile(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long userId) {
        UserProfileResponseDto userProfile = profileService.getProfile(userId);
        return ResponseEntity.ok(userProfile);
    }
}
