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
            user = userRepository.findById(request.getUserId()).orElse(null);
        }

        Event event = Event.builder()
                .title(request.getTitle())
                .type(request.getType())
                .date(request.getDate())
                .ubication(request.getUbication())
                .description(request.getDescription())
                .user(user)
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
    public EventDTO toDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setType(event.getType());
        dto.setDate(event.getDate());
        dto.setUbication(event.getUbication());
        dto.setDescription(event.getDescription());
        dto.setImageUrl(event.getImageUrl());
        dto.setIsEnabled(event.getIsEnabled());
        if (event.getUser() != null) {
            dto.setUserId(event.getUser().getId());
        }
        return dto;
    }
}