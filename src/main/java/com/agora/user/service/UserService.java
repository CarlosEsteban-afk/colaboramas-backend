package com.agora.user.service;

import com.agora.auth.model.Role;
import com.agora.auth.model.RoleEnum;
import com.agora.tag.model.Keyword;
import com.agora.tag.model.KeywordType;
import com.agora.tag.repository.KeywordRepository;
import com.agora.user.dto.CompleteProfileDTO;
import com.agora.user.dto.UserCardDTO;
import com.agora.user.model.UserKeyword;
import com.agora.user.repository.RoleRepository;
import com.agora.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.agora.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeywordRepository keywordRepository;
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, KeywordRepository keywordRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.keywordRepository = keywordRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(String username, String email, String password, Set<RoleEnum> roles) {
        Set<Role> roleEntities = new HashSet<>(roleRepository.findRolesByRoleNameIn((List<RoleEnum>) roles));

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


    public User completeUserProfile(Long userId, CompleteProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));

        user.setPais(dto.getPais());
        user.setCiudad(dto.getCiudad());
        user.setMotivaciones(dto.getMotivaciones());
        user.setActividadesPersonales(dto.getActividades());
        user.setProyectosRecientes(dto.getProyectos());

        // === Educación (por ahora solo imprimir o guardar simple) ===
        if (dto.getEducacion() != null && !dto.getEducacion().isBlank()) {
            System.out.println("Educación registrada: " + dto.getEducacion());
        }

        // === Limpieza previa de keywords antiguas (si se quiere sobrescribir) ===
        user.getKeywords().clear();

        // === Procesar keywords desde los campos de texto ===
        addKeywordsFromText(user, dto.getInvestigacion(), KeywordType.CAMPO_INVESTIGACION);
        addKeywordsFromText(user, dto.getIntereses(), KeywordType.LINEA_INTERES);

        return userRepository.save(user);
    }

    /**
     * Divide el texto de entrada en tags individuales y los asocia al usuario.
     * Ejemplo: "IA, Ciencia de datos; Machine Learning" → ["IA", "Ciencia de datos", "Machine Learning"]
     */
    private void addKeywordsFromText(User user, String text, KeywordType type) {
        if (text == null || text.isBlank()) return;

        List<String> keywordNames = Arrays.stream(text.split("[,;\\n]"))
                .map(String::trim)
                .filter(k -> !k.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        for (String kwName : keywordNames) {
            Keyword keyword = keywordRepository.findByName(kwName)
                    .orElseGet(() -> {
                        Keyword newKeyword = new Keyword();
                        newKeyword.setName(kwName);
                        return keywordRepository.save(newKeyword);
                    });
            UserKeyword userKeyword = new UserKeyword();
            userKeyword.setUser(user);
            userKeyword.setKeyword(keyword);
            userKeyword.setType(type);
            user.addKeyword(userKeyword);
        }
    }


}
