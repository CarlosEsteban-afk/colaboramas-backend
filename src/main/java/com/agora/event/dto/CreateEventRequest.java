package com.agora.event.dto;

import com.agora.event.model.EventType;
import lombok.Data;

import java.time.LocalDateTime;
// Para recibir los datos al crear un nuevo evento.
@Data
public class CreateEventRequest {
    private String title;
    private EventType type;
    private LocalDateTime date;
    private String ubication;
    private String description;
    private Long userId; // ID del usuario que crea el evento (opcional)
}