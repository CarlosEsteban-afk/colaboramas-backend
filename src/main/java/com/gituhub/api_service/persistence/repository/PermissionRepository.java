package com.gituhub.api_service.persistence.repository;

import com.gituhub.api_service.persistence.entity.PermissionEntity;
import com.gituhub.api_service.persistence.entity.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByPermissionName(PermissionEnum permissionName);
}