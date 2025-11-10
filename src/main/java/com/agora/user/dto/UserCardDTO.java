package com.agora.user.dto;

import com.agora.profile.model.Educacion;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserCardDTO {
    private Long id;
    private String username;
    private String imageUrl;
    private Set<Educacion> historialEducativo;
    private String motivaciones;
    private String actividadesPersonales;
    private String proyectosRecientes;
}
