package com.agora.user.controller;

import com.agora.user.dto.CreateUserRequest;
import com.agora.user.dto.UserDTO;
import com.agora.user.model.User;
import com.agora.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers(){
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


}
