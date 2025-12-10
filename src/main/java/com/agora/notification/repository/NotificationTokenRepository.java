package com.agora.notification.repository;

import com.agora.notification.model.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {
    Optional<NotificationToken> findByFirebaseToken(String firebaseToken);
    Optional<NotificationToken> findByUserId(Long userId);
}
