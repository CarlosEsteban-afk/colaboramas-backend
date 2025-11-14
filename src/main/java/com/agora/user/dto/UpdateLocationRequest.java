package com.agora.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLocationRequest {

    @NotNull(message = "La latitud no puede ser nula.")
    private Double latitud;

    @NotNull(message = "La longitud no puede ser nula.")
    private Double longitud;
}
