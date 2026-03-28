package com.agora.notification.controller;

import com.agora.notification.dto.RegisterTokenRequest;
import com.agora.notification.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.agora.user.repository.UserRepository;
import com.agora.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final PushNotificationService pushNotificationService;
    private final UserRepository userRepository;

    @PostMapping("/register-token")
    public ResponseEntity<String> registerToken(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody RegisterTokenRequest request
    ) {
        logger.info("📥 Received token registration request: {}", request);
        logger.info("   firebaseToken: {}", request.getFirebaseToken());
        logger.info("   platform: {}", request.getPlatform());
        
        // Validate request has token
        if (request.getFirebaseToken() == null || request.getFirebaseToken().trim().isEmpty()) {
            logger.error("❌ Firebase token is null or empty!");
            return ResponseEntity.badRequest().body("Firebase token is required");
        }
        
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        pushNotificationService.registerToken(
            user.getId(),
            request.getFirebaseToken(),
            request.getPlatform()
        );
        logger.info("✅ Firebase token registered for user: {} on platform: {}", user.getEmail(), request.getPlatform());
        return ResponseEntity.ok("Firebase token registered successfully");
    }

    @PostMapping("/test")
    public ResponseEntity<String> sendTestNotification(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("🚀 Sending test notification to user: {}", userDetails.getUsername());
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        pushNotificationService.sendNotificationToUser(
            user.getId(),
            "Test Notification",
            "This is a test notification from Colaboramas"
        );
        return ResponseEntity.ok("Test notification sent");
    }
}
