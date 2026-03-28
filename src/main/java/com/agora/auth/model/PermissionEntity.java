package com.agora.auth.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "permission")
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_name", unique = true, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private PermissionEnum permissionName;

    @ManyToMany(mappedBy = "permissionEntities", fetch = FetchType.EAGER)
    @Builder.Default
    @JsonBackReference(value = "role-permissions")
    private Set<Role> roleEntities = new HashSet<>();

}
