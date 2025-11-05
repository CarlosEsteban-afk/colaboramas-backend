package com.agora.config;

import com.agora.persistence.PermissionEntity;
import com.agora.persistence.PermissionEnum;
import com.agora.repository.PermissionRepository;
import com.agora.repository.RoleRepository;
import com.agora.repository.UserRepository;
import com.agora.user.model.Role;
import com.agora.user.model.RoleEnum;
import com.agora.user.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        return args -> {
            List<PermissionEntity> permissions = List.of(
                    PermissionEntity.builder().permissionName(PermissionEnum.VER_RECOMENDACIONES).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.BUSCAR_USUARIO).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.CONTACTAR_USUARIO).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.VER_EVENTOS).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.AGREGAR_EVENTO).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.EDITAR_EVENTO).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.ELIMINAR_EVENTO).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.VER_PERFIL).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.EDITAR_PERFIL).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.ELIMINAR_PERFIL).build(),
                    PermissionEntity.builder().permissionName(PermissionEnum.VER_NOTIFICACIONES).build()
            );

            permissions.forEach(p ->
                    permissionRepository.findByPermissionName(p.getPermissionName())
                            .orElseGet(() -> permissionRepository.save(p))
            );

            var allPermissions = permissionRepository.findAll();

            Role academico = Role.builder()
                    .roleName(RoleEnum.ACADEMICO)
                    .permissionEntities(Set.copyOf(allPermissions))
                    .build();

            Role comunicador = Role.builder()
                    .roleName(RoleEnum.COMUNICADOR)
                    .permissionEntities(Set.of(
                            permissionRepository.findByPermissionName(PermissionEnum.VER_PERFIL).get(),
                            permissionRepository.findByPermissionName(PermissionEnum.EDITAR_PERFIL).get(),
                            permissionRepository.findByPermissionName(PermissionEnum.VER_RECOMENDACIONES).get()
                    ))
                    .build();

            roleRepository.findByRoleName(RoleEnum.ACADEMICO).orElseGet(() -> roleRepository.save(academico));
            roleRepository.findByRoleName(RoleEnum.COMUNICADOR).orElseGet(() -> roleRepository.save(comunicador));

            var encoder = new BCryptPasswordEncoder();

            if (userRepository.findUserByUsername("academico_test@email.com").isEmpty()) {
                User academicoUser = User.builder()
                        .name("Academico Test")
                        .username("academico_test@email.com")
                        .password(encoder.encode("123"))
                        .roleEntities(Set.of(roleRepository.findByRoleName(RoleEnum.ACADEMICO).get()))
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .isEnabled(true)
                        .build();
                userRepository.save(academicoUser);
            }

            if (userRepository.findUserByUsername("comunicador_test@email.com").isEmpty()) {
                User comunicadorUser = User.builder()
                        .name("Comunicador Test")
                        .username("comunicador_test@email.com")
                        .password(encoder.encode("123"))
                        .roleEntities(Set.of(roleRepository.findByRoleName(RoleEnum.COMUNICADOR).get()))
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .isEnabled(true)
                        .build();
                userRepository.save(comunicadorUser);
            }

            System.out.println("Roles, permisos y usuarios inicializados correctamente.");
        };
    }
}
