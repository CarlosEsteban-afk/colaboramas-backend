package com.agora.profile.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserProfileResponseDto {
    private Long id;
    private String username;
    private String email;
    private List<EducacionDto> historialEducativo;
    private List<KeywordDto> keywords;
    private String motivaciones;
    private String actividadesPersonales;
    private String proyectosRecientes;
}
