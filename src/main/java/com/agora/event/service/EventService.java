package com.agora.event.service;

import com.agora.event.dto.CreateEventRequest;
import com.agora.event.dto.EventDTO;
import com.agora.event.model.Event;
import com.agora.event.repository.EventRepository;
import com.agora.user.model.User;
import com.agora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository; // Para buscar al usuario

    public EventDTO createEvent(CreateEventRequest request) {
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found")); // Manejar mejor la excepción
        }

        Event event = Event.builder()
                .title(request.getTitle())
                .type(request.getType())
                .date(request.getDate())
                .ubication(request.getUbication())
                .description(request.getDescription())
                .user(user) // Puede ser null
                .build();

        Event savedEvent = eventRepository.save(event);
        return toDTO(savedEvent);
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Convierte una entidad Event a un EventDTO
    private EventDTO toDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setType(event.getType());
        dto.setDate(event.getDate());
        dto.setUbication(event.getUbication());
        dto.setDescription(event.getDescription());
        // Si el usuario no es nulo, usa su nombre, si no, "Scraper"
        dto.setCreatedBy(event.getUser() != null ? event.getUser().getUsername() : "Scraper");
        return dto;
    }
}