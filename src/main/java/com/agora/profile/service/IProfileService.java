package com.agora.profile.service;

import com.agora.profile.dto.UpdateProfileRequest;
import com.agora.profile.dto.UserProfileResponseDto;

public interface IProfileService {
    UserProfileResponseDto updateProfile(Long userId, UpdateProfileRequest request);
    UserProfileResponseDto getProfile(Long userId);
}
