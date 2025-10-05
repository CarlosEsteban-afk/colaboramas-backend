package com.gituhub.api_service.controller;

import com.gituhub.api_service.controller.dto.AuthCreateUserRequest;
import com.gituhub.api_service.controller.dto.AuthLoginRequest;
import com.gituhub.api_service.controller.dto.AuthResponse;
import com.gituhub.api_service.persistence.entity.UserEntity;
import com.gituhub.api_service.persistence.repository.UserRepository;
import com.gituhub.api_service.service.UserDetailsServiceImpl;
import com.gituhub.api_service.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Claim;
import java.util.*;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> createUser(@RequestBody @Valid AuthCreateUserRequest authCreateUserRequest){
        return new ResponseEntity<>(this.userDetailsService.createUser(authCreateUserRequest), HttpStatus.CREATED);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwt);
            String username = jwtUtils.extractUsername(decodedJWT);

            UserEntity user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!user.isEnabled()) {
                throw new RuntimeException("User is disabled");
            }

            Claim authoritiesClaim = jwtUtils.getSpecificClaim(decodedJWT, "authorities");
            String authorities = authoritiesClaim.asString();

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("username", username);
            response.put("authorities", Arrays.asList(authorities.split(",")));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
