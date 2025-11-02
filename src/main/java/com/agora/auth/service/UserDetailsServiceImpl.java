package com.agora.auth.service;

import com.agora.auth.dto.AuthCreateRoleRequest;
import com.agora.auth.dto.AuthCreateUserRequest;
import com.agora.auth.dto.AuthLoginRequest;
import com.agora.auth.dto.AuthResponse;
import com.agora.user.model.Role;
import com.agora.user.model.RoleEnum;
import com.agora.user.model.User;
import com.agora.repository.RoleRepository;
import com.agora.repository.UserRepository;
import com.agora.util.JwtUtils;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        user.getRoleEntities()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName().name()))));

        user.getRoleEntities().stream()
                .flatMap(role -> role.getPermissionEntities().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName().name())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                authorityList);
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();
        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        AuthResponse authResponse = new AuthResponse(username, "user logged successfully", accessToken, true);
        return authResponse;
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException("Invalid username or password");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {

        String name = authCreateUserRequest.name();
        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();
        List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();

        List<RoleEnum> roleEnums = roleRequest.stream()
                .map(RoleEnum::valueOf)
                .collect(Collectors.toList());

        Set<Role> roleSet = roleRepository.findRolesByRoleNameIn(roleEnums).stream().collect(Collectors.toSet());
        if(roleSet.isEmpty()){
            throw new IllegalIdentifierException("The specified role does not exist");
        }

        User user = User.builder()
                .name(name)
                .username(username)
                .password(passwordEncoder.encode(password))
                .roleEntities(roleSet)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .build();

        User userCreated = userRepository.save(user);
        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userCreated.getRoleEntities().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName().name()))));
        userCreated.getRoleEntities()
                .stream()
                .flatMap(role -> role.getPermissionEntities().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName().name())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(), userCreated.getPassword(), authorityList);
        String accessToken = jwtUtils.createToken(authentication);
        AuthResponse authResponse = new AuthResponse(userCreated.getUsername(), "user created successfully", accessToken, true);
        return authResponse;
    }

    private AuthCreateRoleRequest createRoleRequest(List<String> roleList) {
        if (roleList == null || roleList.isEmpty()) {
            throw new IllegalArgumentException("Role list cannot be null or empty");
        }
        for (String role : roleList) {
            try {
                RoleEnum.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
        }
        return new AuthCreateRoleRequest(roleList);
    }
}
