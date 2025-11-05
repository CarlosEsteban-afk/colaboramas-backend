package com.agora.user.service;

import com.agora.auth.model.Role;
import com.agora.auth.model.RoleEnum;
import com.agora.auth.repository.RoleRepository;
import com.agora.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.agora.user.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(String name, String username, String password, Set<RoleEnum> roles) {
        Set<Role> roleEntities = new HashSet<>(roleRepository.findRolesByRoleNameIn((List<RoleEnum>) roles));

        if (roleEntities.isEmpty()) {
            throw new IllegalArgumentException("Roles not found");
        }

        User user = User.builder()
                .name(name)
                .username(username)
                .password(passwordEncoder.encode(password))
                .roleEntities(roleEntities)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .build();

        return userRepository.save(user);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
