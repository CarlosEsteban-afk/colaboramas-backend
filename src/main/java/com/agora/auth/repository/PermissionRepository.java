package com.agora.auth.repository;

import com.agora.auth.model.PermissionEntity;
import com.agora.auth.model.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByPermissionName(PermissionEnum permissionName);
}