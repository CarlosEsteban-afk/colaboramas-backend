package com.agora.user.service;

import com.agora.auth.model.Role;
import com.agora.auth.model.RoleEnum;
import com.agora.user.dto.UpdateLocationRequest;
import com.agora.user.dto.UserCardDTO;
import com.agora.user.repository.RoleRepository;
import com.agora.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agora.user.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
    }

    @Transactional
    public void updateUserLocation(UpdateLocationRequest locationRequest) {
        User currentUser = getCurrentUser();
        currentUser.setLatitud(locationRequest.getLatitud());
        currentUser.setLongitud(locationRequest.getLongitud());
        userRepository.save(currentUser);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(String username, String email, String password, Set<RoleEnum> roles) {
        Set<Role> roleEntities = new HashSet<>(roleRepository.findRolesByRoleNameIn(new ArrayList<>(roles)));

        if (roleEntities.isEmpty()) {
            throw new IllegalArgumentException("Roles not found");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roleEntities)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .isEnabled(true)
                .build();

        return userRepository.save(user);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsEnabled(false);
        userRepository.save(user);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public List<UserCardDTO> getAllUserCards() {
        String currentUsername="manolo";

        return userRepository.findAll().stream()
                .filter(u -> !u.getUsername().equals(currentUsername))
                .map(u -> UserCardDTO.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .imageUrl(u.getImageUrl())
                        .historialEducativo(u.getHistorialEducativo())
                        .motivaciones(u.getMotivaciones())
                        .actividadesPersonales(u.getActividadesPersonales())
                        .proyectosRecientes(u.getProyectosRecientes())
                        .build())
                .collect(Collectors.toList());
    }


    public User updateUserImageUrl(Long userId, String imageUrl) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con id: " + userId);
        }

        User user = optionalUser.get();
        user.setImageUrl(imageUrl);
        return userRepository.save(user);
    }


}
