package com.agora.auth.controller;

import com.agora.auth.dto.AuthCreateUserRequest;
import com.agora.auth.dto.AuthLoginRequest;
import com.agora.auth.dto.AuthResponse;
import com.agora.auth.service.AuthService;
import com.agora.auth.service.OrcidService;
import com.agora.auth.service.UserDetailsServiceImpl;
import com.agora.user.dto.UserResponseDTO;
import com.agora.user.repository.UserRepository;
import com.agora.user.model.User;
import com.agora.user.service.UserService;
import com.agora.util.JwtUtils;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OrcidService orcidService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Value("${orcid.client-id}")
    private String orcidClientId;

    @Value("${orcid.client-secret}")
    private String orcidClientSecret;

    @Value("${orcid.redirect-uri}")
    private String orcidRedirectUri;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        AuthResponse response = authService.loginUser(userRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest request) {
        AuthResponse response = authService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
    }



    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwt);
            String email = jwtUtils.extractUsername(decodedJWT);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!user.getIsEnabled()) {
                throw new RuntimeException("User is disabled");
            }

            Claim authoritiesClaim = jwtUtils.getSpecificClaim(decodedJWT, "authorities");
            String authorities = authoritiesClaim.asString();

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("email", email);
            response.put("authorities", Arrays.asList(authorities.split(",")));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("valid", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @GetMapping("/login/orcid")
    public void orcidLogin(HttpServletResponse response) throws IOException {
        String orcidAuthUrl = "https://sandbox.orcid.org/oauth/authorize" +
                "?client_id=" + orcidClientId +
                "&response_type=code" +
                "&scope=/authenticate" +
                "&redirect_uri=" + orcidRedirectUri;
        response.sendRedirect(orcidAuthUrl);
    }

    @GetMapping("/login/orcid/callback")
    public ResponseEntity<AuthResponse> orcidCallback(@RequestParam("code") String code) {
        String orcidId = orcidService.getOrcidId(code);
        AuthResponse response = authService.loginWithOrcid(orcidId);
        return ResponseEntity.ok(response);
    }
}
