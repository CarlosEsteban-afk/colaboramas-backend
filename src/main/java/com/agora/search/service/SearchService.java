package com.agora.search.service;

import com.agora.profile.model.Educacion;
import com.agora.search.dto.ProfileResponse;
import com.agora.user.model.User;
import com.agora.user.model.UserKeyword;
import com.agora.profile.repository.EducacionRepository; // Necesitarás este repo
import com.agora.user.repository.UserKeywordRepository; // Y este
import com.agora.user.repository.UserRepository;
import com.agora.tag.model.KeywordType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepository userRepository;
    private final UserKeywordRepository userKeywordRepository;
    private final EducacionRepository educacionRepository;

    @Transactional(readOnly = true)
    public List<ProfileResponse> recommendProfiles() {
        User currentUser = getCurrentUser();
        Long userId = currentUser.getId();

        List<Object[]> results = userRepository.findRecommendedUsersByProximity(
                userId,
                currentUser.getLatitud(),
                currentUser.getLongitud());
        
        return buildProfileResponsesFromResults(results);
    }

    @Transactional(readOnly = true)
    public List<ProfileResponse> getRecommendedByRelevance() {
        User currentUser = getCurrentUser();
        Long userId = currentUser.getId();

        List<Object[]> results = userRepository.findRecommendedUsersByRelevance(userId);

        return buildProfileResponsesFromResults(results);
    }

    /**
     * Este método privado resuelve el problema N+1
     */
  private List<ProfileResponse> buildProfileResponsesFromResults(List<Object[]> results) {
        if (results.isEmpty()) {
            return List.of();
        }

        // 1. Extraer los IDs de usuario MANTENIENDO EL ORDEN ORIGINAL
        List<Long> orderedUserIds = results.stream()
                .map(row -> ((Number) row[0]).longValue())
                .toList();

        // 2. Obtener todas las colecciones en UNA SOLA consulta (Solución N+1)
        List<UserKeyword> allKeywords = userKeywordRepository.findByUserIdInWithDetails(orderedUserIds);
        List<Educacion> allEducacion = educacionRepository.findFirstByUserIdIn(orderedUserIds);

        // 3. Agrupar colecciones por usuario para acceso rápido
        Map<Long, Set<UserKeyword>> keywordsByUserId = allKeywords.stream()
                .collect(Collectors.groupingBy(kw -> kw.getUser().getId(), Collectors.toSet()));
        
        Map<Long, Educacion> educacionByUserId = allEducacion.stream()
                .collect(Collectors.toMap(edu -> edu.getUser().getId(), Function.identity()));

        // 4. Obtener los objetos User
        Map<Long, User> userMap = userRepository.findAllById(orderedUserIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // 5. Construir la respuesta final ITERANDO SOBRE LA LISTA ORDENADA
        return orderedUserIds.stream()
                .map(userId -> {
                    User user = userMap.get(userId);
                    Set<UserKeyword> keywords = keywordsByUserId.getOrDefault(userId, Set.of());
                    Educacion educacion = educacionByUserId.get(userId);

                    // Llamamos al constructor original, sin score ni distancia
                    return buildProfileResponse(user, keywords, educacion);
                })
                .toList();
    }

    // He simplificado este método para que coincida con tu código original
    private ProfileResponse buildProfileResponse(User user, Set<UserKeyword> keywords, Educacion educacion) {
        return ProfileResponse.builder()
                .id(user.getId())
                .nombre(user.getUsername())
                .imageUrl(user.getImageUrl())
                .pais(user.getPais())
                .ciudad(user.getCiudad())
                .profesion(educacion != null ? educacion.getTitulo() : "N/A")
                .camposInvestigacion(getKeywordsValues(keywords, KeywordType.CAMPO_INVESTIGACION))
                .lineasInteres(getKeywordsValues(keywords, KeywordType.LINEA_INTERES))
                .build();
    }

    private Set<String> getKeywordsValues(Set<UserKeyword> keywords, KeywordType type) {
        if (keywords == null) return Set.of();
        return keywords.stream()
                .filter(kw -> kw.getType() == type)
                .map(kw -> kw.getKeyword().getName())
                .collect(Collectors.toSet());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
    }
}