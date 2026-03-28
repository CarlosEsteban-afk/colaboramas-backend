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
    private String pais;
    private String ciudad;
    private Double latitud;
    private Double longitud;
    private String motivaciones;
    private String actividadesPersonales;
    private String proyectosRecientes;
}
