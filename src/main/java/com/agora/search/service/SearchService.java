package com.agora.search.service;

import com.agora.search.dto.ProfileResponse;
import com.agora.user.model.User;
import com.agora.user.model.UserKeyword;
import com.agora.user.repository.UserRepository;
import com.agora.tag.model.KeywordType;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ProfileResponse> recommendProfiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        if (authentication.getPrincipal() instanceof UserDetails) {
            email = ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long userId = currentUser.getId();

        List<Object[]> results = userRepository.findRecommendedUsersByProximity(userId,
                currentUser.getLatitud(),
                currentUser.getLongitud());

        // 1. Extraer los IDs de los usuarios recomendados
        List<Long> recommendedUserIds = results.stream()
                .map(row -> ((Number) row[0]).longValue())
                .toList();

        if (recommendedUserIds.isEmpty()) {
            return List.of();
        }

        // 2. Obtener todos los usuarios en una sola consulta para evitar N+1
        Map<Long, User> userMap = userRepository.findAllById(recommendedUserIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 3. Construir la respuesta usando el mapa de usuarios
        return results.stream()
                .map(row -> {
                    Long recommendedId = ((Number) row[0]).longValue();
                    User user = userMap.get(recommendedId);
                    return buildProfileResponse(user);
                })
                .toList();
    }

    private ProfileResponse buildProfileResponse(User user) {
        return ProfileResponse.builder()
                .id(user.getId())
                .nombre(user.getUsername())
                .imageUrl(user.getImageUrl())
                .pais(user.getPais())
                .ciudad(user.getCiudad())
                .profesion(user.getHistorialEducativo().stream()
                        .findFirst()
                        .map(edu -> edu.getTitulo())
                        .orElse("N/A"))
                .camposInvestigacion(getKeywordsValues(user.getKeywords(), KeywordType.CAMPO_INVESTIGACION))
                .lineasInteres(getKeywordsValues(user.getKeywords(), KeywordType.LINEA_INTERES))
                .build();
    }

    private Set<String> getKeywordsValues(Set<UserKeyword> keywords, KeywordType type) {
        return keywords.stream()
                .filter(kw -> kw.getType() == type)
                .map(kw -> kw.getKeyword().getName())
                .collect(Collectors.toSet());
    }

}
