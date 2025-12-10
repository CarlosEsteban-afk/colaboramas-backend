package com.agora.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.agora.user.model.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String firebaseToken;

    @Column(name = "platform")
    private String platform;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
