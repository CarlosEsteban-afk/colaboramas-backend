package com.agora.user.controller;

import com.agora.user.dto.CompleteProfileDTO;
import com.agora.user.dto.CreateUserRequest;
import com.agora.user.dto.UserCardDTO;
import com.agora.user.dto.UserDTO;
import com.agora.user.model.User;
import com.agora.user.service.UserImageService;
import com.agora.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserImageService userImageService;
    public UserController(UserService userService, UserImageService userImageService) {
        this.userService = userService;
        this.userImageService = userImageService;
    }
    @PostMapping("/upload-image/{userId}")
    public ResponseEntity<String> uploadImage(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = userImageService.uploadUserImage(file);
            userService.updateUserImageUrl(userId, imageUrl);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream().map(u -> new UserDTO(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public UserDTO createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(
                request.username(),
                request.email(),
                request.password(),
                request.roles()
        );
        return new UserDTO(user.getId(), user.getUsername());
    }
    /*@PutMapping("/{userId}/complete-profile")
    public ResponseEntity<UserDTO> completeProfile(
            @PathVariable Long userId,
            @RequestBody CompleteProfileDTO dto
    ) {
        User updatedUser = userService.completeUserProfile(userId, dto);
        return ResponseEntity.ok(new UserDTO(updatedUser.getId(), updatedUser.getUsername()));
    }*/


    @GetMapping("/cards")
    public List<UserCardDTO> getAllUserCards(Authentication auth) {
        return userService.getAllUserCards();
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("@userService.findUserById(#userId).username == authentication.name")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
