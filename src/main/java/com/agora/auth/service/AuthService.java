package com.agora.auth.service;

import com.agora.auth.dto.AuthCreateUserRequest;
import com.agora.auth.dto.AuthLoginRequest;
import com.agora.auth.dto.AuthResponse;
import com.agora.repository.RoleRepository;
import com.agora.repository.UserRepository;
import com.agora.user.model.Role;
import com.agora.user.model.RoleEnum;
import com.agora.user.model.User;
import com.agora.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    public AuthResponse loginUser(AuthLoginRequest request) {
        String username = request.username();
        String password = request.password();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.createToken(authentication);
        return new AuthResponse(username, "User logged successfully", token, true);
    }


    public AuthResponse createUser(AuthCreateUserRequest request) {
        List<RoleEnum> roleEnums = request.roleRequest().roleListName()
                .stream()
                .map(RoleEnum::valueOf)
                .toList();

        Set<Role> roles = new HashSet<>(roleRepository.findRolesByRoleNameIn(roleEnums));

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("The specified role does not exist");
        }

        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roleEntities(roles)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .build();

        userRepository.save(user);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .flatMap(role -> {
                    Stream<SimpleGrantedAuthority> roleAuthority = Stream.of(
                            new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name())
                    );
                    Stream<SimpleGrantedAuthority> permissionAuthorities = role.getPermissionEntities()
                            .stream()
                            .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName().name()));
                   return Stream.concat(roleAuthority, permissionAuthorities);
                })
                .toList();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);
        String token = jwtUtils.createToken(authentication);

        return new AuthResponse(user.getUsername(), "User created successfully", token, true);
    }


}
