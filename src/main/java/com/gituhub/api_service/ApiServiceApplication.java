package com.gituhub.api_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

import com.gituhub.api_service.persistence.entity.*;
import com.gituhub.api_service.persistence.repository.UserRepository;

@SpringBootApplication
public class ApiServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("SPRING_JPA_HIBERNATE_DDL_AUTO", dotenv.get("SPRING_JPA_HIBERNATE_DDL_AUTO"));
		System.setProperty("SPRING_SECURITY_USER_NAME", dotenv.get("SPRING_SECURITY_USER_NAME"));
		System.setProperty("SPRING_SECURITY_USER_PASSWORD", dotenv.get("SPRING_SECURITY_USER_PASSWORD"));
		SpringApplication.run(ApiServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository){
		return args -> {

			// Permissions

			PermissionEntity VER_RECOMENDACIONES = PermissionEntity.builder()
					.permissionName(PermissionEnum.VER_RECOMENDACIONES)
					.build();

			PermissionEntity BUSCAR_USUARIO = PermissionEntity.builder()
					.permissionName(PermissionEnum.BUSCAR_USUARIO)
					.build();

			PermissionEntity CONTACTAR_USUARIO = PermissionEntity.builder()
					.permissionName(PermissionEnum.CONTACTAR_USUARIO)
					.build();

			PermissionEntity VER_EVENTOS = PermissionEntity.builder()
					.permissionName(PermissionEnum.VER_EVENTOS)
					.build();

			PermissionEntity AGREGAR_EVENTO = PermissionEntity.builder()
					.permissionName(PermissionEnum.AGREGAR_EVENTO)
					.build();

			PermissionEntity EDITAR_EVENTO = PermissionEntity.builder()
					.permissionName(PermissionEnum.EDITAR_EVENTO)
					.build();

			PermissionEntity ELIMINAR_EVENTO = PermissionEntity.builder()
					.permissionName(PermissionEnum.ELIMINAR_EVENTO)
					.build();

			PermissionEntity VER_PERFIL = PermissionEntity.builder()
					.permissionName(PermissionEnum.VER_PERFIL)
					.build();

			PermissionEntity EDITAR_PERFIL = PermissionEntity.builder()
					.permissionName(PermissionEnum.EDITAR_PERFIL)
					.build();

			PermissionEntity ELIMINAR_PERFIL = PermissionEntity.builder()
					.permissionName(PermissionEnum.ELIMINAR_PERFIL)
					.build();

			PermissionEntity VER_NOTIFICACIONES = PermissionEntity.builder()
					.permissionName(PermissionEnum.VER_NOTIFICACIONES)
					.build();

			// Roles

			RoleEntity ACADEMICO = RoleEntity.builder()
					.roleName(RoleEnum.ACADEMICO)
					.permissionEntities(Set.of(VER_RECOMENDACIONES, BUSCAR_USUARIO, CONTACTAR_USUARIO, VER_EVENTOS, AGREGAR_EVENTO, EDITAR_EVENTO, ELIMINAR_EVENTO, VER_PERFIL, EDITAR_PERFIL, ELIMINAR_PERFIL, VER_NOTIFICACIONES))
					.build();

			RoleEntity COMUNICADOR = RoleEntity.builder()
					.roleName(RoleEnum.COMUNICADOR)
					.permissionEntities(Set.of(VER_RECOMENDACIONES, BUSCAR_USUARIO, CONTACTAR_USUARIO, VER_PERFIL, EDITAR_PERFIL, ELIMINAR_PERFIL, VER_NOTIFICACIONES))
					.build();

			// Users Test

			UserEntity ACADEMICO_TEST = UserEntity.builder()
					.username("academico_test@email.com")
					.password(new BCryptPasswordEncoder().encode("123"))
					.roleEntities(Set.of(ACADEMICO))
					.accountNonExpired(true)
					.accountNonLocked(true)
					.credentialsNonExpired(true)
					.isEnabled(true)
					.build();

			UserEntity COMUNICADOR_TEST = UserEntity.builder()
					.username("comunicador_test@email.com")
					.password(new BCryptPasswordEncoder().encode("123"))
					.roleEntities(Set.of(COMUNICADOR))
					.accountNonExpired(true)
					.accountNonLocked(true)
					.credentialsNonExpired(true)
					.isEnabled(true)
					.build();

			userRepository.saveAll(List.of(ACADEMICO_TEST, COMUNICADOR_TEST));
		};
	}
}
