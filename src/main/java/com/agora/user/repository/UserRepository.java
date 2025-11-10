package com.agora.user.repository;

import com.agora.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findUserByUsername(String username);

    @Query("SELECT u, COUNT(DISTINCT uk.keyword) as commonKeywords " +
            "FROM User u JOIN u.keywords uk " +
            "WHERE uk.keyword IN (SELECT uk2.keyword FROM User u2 JOIN u2.keywords uk2 WHERE u2.id = :userId) " +
            "AND u.id != :userId " +
            "GROUP BY u " +
            "ORDER BY commonKeywords DESC")
    List<Object[]> findRecommendedUsersByRelevance(@Param("userId") Long userId);

    @Query(value = """
            WITH Relevancia AS (
                SELECT
                    u.id,
                    COUNT(DISTINCT uk_otros.keyword_id) AS commonKeywords
                FROM
                    user_keywords AS uk_actual
                JOIN
                    user_keywords AS uk_otros ON uk_actual.keyword_id = uk_otros.keyword_id
                JOIN
                    users AS u ON uk_otros.user_id = u.id
                WHERE
                    uk_actual.user_id = :userId
                    AND uk_otros.user_id != :userId
                GROUP BY
                    u.id
            )
            SELECT
                u.id,
                u.username,
                r.commonKeywords,
                (earth_distance(
                    ll_to_earth(:userLat, :userLon), -- Ubicación del usuario actual    
                    ll_to_earth(u.latitud, u.longitud)    -- Ubicación del otro usuario
                ) / 1000) AS distanciaEnKm -- Convertir metros a KM
            FROM
                Relevancia r
            JOIN
                users u ON r.id = u.id
            WHERE
                u.latitud IS NOT NULL AND u.longitud IS NOT NULL
            ORDER BY
                distanciaEnKm ASC,
                commonKeywords DESC
            LIMIT 20
            """, nativeQuery = true)
    List<Object[]> findRecommendedUsersByProximity(
            @Param("userId") Long userId,
            @Param("userLat") Double userLat,
            @Param("userLon") Double userLon);

}
