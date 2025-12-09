package com.agora.auth.model;

import com.agora.user.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_has_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @Builder.Default
        @JsonManagedReference(value = "role-permissions")
        private Set<PermissionEntity> permissionEntities = new HashSet<>();

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @Builder.Default
    @JsonBackReference(value = "user-roles")
    private Set<User> users = new HashSet<>();
}
