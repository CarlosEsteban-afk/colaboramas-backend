package com.agora;

import com.agora.user.model.User;
import com.agora.persistence.PermissionEntity;
import com.agora.persistence.PermissionEnum;
import com.agora.user.model.Role;
import com.agora.user.model.RoleEnum;
import com.agora.repository.PermissionRepository;
import com.agora.user.repository.RoleRepository;
import com.agora.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;


@SpringBootApplication
public class ApiServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();


        //System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("SPRING_JPA_HIBERNATE_DDL_AUTO", dotenv.get("SPRING_JPA_HIBERNATE_DDL_AUTO"));
		System.setProperty("SPRING_SECURITY_USER_NAME", dotenv.get("SPRING_SECURITY_USER_NAME"));
		System.setProperty("SPRING_SECURITY_USER_PASSWORD", dotenv.get("SPRING_SECURITY_USER_PASSWORD"));
		System.setProperty("SECURITY.JWT.USER.GENERATOR", dotenv.get("SECURITY.JWT.USER.GENERATOR"));
		System.setProperty("SECURITY.JWT.KEY.PRIVATE", dotenv.get("SECURITY.JWT.KEY.PRIVATE"));
		SpringApplication.run(ApiServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PermissionRepository permissionRepository) {
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

			var VER_RECOMENDACIONES = permissionRepository.findByPermissionName(PermissionEnum.VER_RECOMENDACIONES).get();
			var BUSCAR_USUARIO = permissionRepository.findByPermissionName(PermissionEnum.BUSCAR_USUARIO).get();
			var CONTACTAR_USUARIO = permissionRepository.findByPermissionName(PermissionEnum.CONTACTAR_USUARIO).get();
			var VER_EVENTOS = permissionRepository.findByPermissionName(PermissionEnum.VER_EVENTOS).get();
			var AGREGAR_EVENTO = permissionRepository.findByPermissionName(PermissionEnum.AGREGAR_EVENTO).get();
			var EDITAR_EVENTO = permissionRepository.findByPermissionName(PermissionEnum.EDITAR_EVENTO).get();
			var ELIMINAR_EVENTO = permissionRepository.findByPermissionName(PermissionEnum.ELIMINAR_EVENTO).get();
			var VER_PERFIL = permissionRepository.findByPermissionName(PermissionEnum.VER_PERFIL).get();
			var EDITAR_PERFIL = permissionRepository.findByPermissionName(PermissionEnum.EDITAR_PERFIL).get();
			var ELIMINAR_PERFIL = permissionRepository.findByPermissionName(PermissionEnum.ELIMINAR_PERFIL).get();
			var VER_NOTIFICACIONES = permissionRepository.findByPermissionName(PermissionEnum.VER_NOTIFICACIONES).get();

			Role ACADEMICO = Role.builder()
					.roleName(RoleEnum.ACADEMICO)
					.permissionEntities(Set.of(
							VER_RECOMENDACIONES, BUSCAR_USUARIO, CONTACTAR_USUARIO,
							VER_EVENTOS, AGREGAR_EVENTO, EDITAR_EVENTO, ELIMINAR_EVENTO,
							VER_PERFIL, EDITAR_PERFIL, ELIMINAR_PERFIL, VER_NOTIFICACIONES))
					.build();

			Role COMUNICADOR = Role.builder()
					.roleName(RoleEnum.COMUNICADOR)
					.permissionEntities(Set.of(
							VER_RECOMENDACIONES, BUSCAR_USUARIO, CONTACTAR_USUARIO,
							VER_PERFIL, EDITAR_PERFIL, ELIMINAR_PERFIL, VER_NOTIFICACIONES))
					.build();

			if (roleRepository.findByRoleName(RoleEnum.ACADEMICO).isEmpty()) {
				roleRepository.save(ACADEMICO);
			}
			if (roleRepository.findByRoleName(RoleEnum.COMUNICADOR).isEmpty()) {
				roleRepository.save(COMUNICADOR);
			}

			var encoder = new BCryptPasswordEncoder();

			if (userRepository.findByEmail("academico_test1@email.com").isEmpty()) {

				User ACADEMICO_TEST = User.builder()
						.username("academico_test1")
						.email("academico_test1@email.com")
						.password(encoder.encode("123"))
						.roles(Set.of(roleRepository.findByRoleName(RoleEnum.ACADEMICO).get()))
						.isEnabled(true)
						.accountNoExpired(true)
						.accountNoLocked(true)
						.credentialNoExpired(true)
						.build();
				userRepository.save(ACADEMICO_TEST);
			}

			if (userRepository.findByEmail("comunicador_test1@email.com").isEmpty()) {

				User COMUNICADOR_TEST = User.builder()
						.username("comunicador_test1")
						.email("comunicador_test1@email.com")
						.password(encoder.encode("123"))
						.roles(Set.of(roleRepository.findByRoleName(RoleEnum.COMUNICADOR).get()))
						.isEnabled(true)
						.accountNoExpired(true)
						.accountNoLocked(true)
						.credentialNoExpired(true)
						.build();
				userRepository.save(COMUNICADOR_TEST);
			}
		
		};
	}
}
