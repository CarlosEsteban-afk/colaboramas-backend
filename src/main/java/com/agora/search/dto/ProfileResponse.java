package com.agora.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private Long id;
    private String nombre;
    private String imageUrl;
    private String pais;
    private String ciudad;
    private String profesion;
    private Set<String> camposInvestigacion;
    private Set<String> lineasInteres;
}