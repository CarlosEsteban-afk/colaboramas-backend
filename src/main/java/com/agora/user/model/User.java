package com.agora.user.model;

import com.agora.profile.model.Educacion;
import com.agora.auth.model.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name="image_url")
    private String imageUrl;

    @Column(nullable = false)
    private String password;

    // --- Campos de Estado (como antes) ---
    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "account_no_expired")
    private Boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private Boolean accountNoLocked;

    @Column(name = "credential_no_expired")
    private Boolean credentialNoExpired;

    // --- Historial Educativo (Estructurado) ---
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Educacion> historialEducativo = new HashSet<>();

    // --- Etiquetas para Búsqueda (Estructurado) ---
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserKeyword> keywords = new HashSet<>();

    // --- Campos Descriptivos (Texto Largo) ---
    @Column(columnDefinition = "TEXT")
    private String motivaciones;

    @Column(columnDefinition = "TEXT")
    private String actividadesPersonales;

    @Column(columnDefinition = "TEXT")
    private String proyectosRecientes;

    // --- Relación con Roles (como antes) ---
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}

