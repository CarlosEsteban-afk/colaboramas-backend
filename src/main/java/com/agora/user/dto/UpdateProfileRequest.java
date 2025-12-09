package com.agora.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProfileRequest {

    private String username;
    private String email;

    private String ciudad;
    private String pais;

    private String motivaciones;
    private String actividadesPersonales;
    private String proyectosRecientes;

    private String imageUrl;
}
