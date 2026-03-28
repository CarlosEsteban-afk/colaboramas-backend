package com.agora.event.dto;

import com.agora.event.model.EventType;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
// Para enviar los datos de un evento al cliente.
@Data
public class EventDTO {
    private Long id;
    private String title;
    private EventType type;
    private LocalDateTime date;
    private String ubication;
    private String description;
    private String createdBy; // Nombre del usuario o "Scraper"
    private Long userId; // Id del usuario que creó el evento (agregado)
    private String imageUrl; // URL de la imagen del evento
    private Boolean isEnabled;

    @JsonProperty("image_url")
    public String getImage_url() {
        return this.imageUrl;
    }

    @JsonProperty("is_enabled")
    public Boolean getIsEnabled() {
        return this.isEnabled;
    }
}