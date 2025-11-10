package com.agora.profile.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateProfileRequest {
    private List<EducacionDto> historialEducativo;
    private List<KeywordDto> keywords;
    private String pais;
    private String ciudad;
    private String motivaciones;
    private String actividadesPersonales;
    private String proyectosRecientes;
}
