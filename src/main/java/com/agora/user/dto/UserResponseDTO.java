package com.agora.user.dto;

import com.agora.auth.model.Role;
import com.agora.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String imageUrl;
    private String motivaciones;
    private String actividadesPersonales;
    private String proyectosRecientes;
    private Set<String> roles;



    public static UserResponseDTO fromEntity(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .motivaciones(user.getMotivaciones())
                .actividadesPersonales(user.getActividadesPersonales())
                .proyectosRecientes(user.getProyectosRecientes())
                .roles(user.getRoles().stream()
                        .map(role -> role.getRoleName().name())
                        .collect(Collectors.toSet()))
                .build();
    }
}
