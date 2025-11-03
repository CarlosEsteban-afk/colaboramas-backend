package com.agora.repository;

import com.agora.persistence.PermissionEntity;
import com.agora.persistence.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByPermissionName(PermissionEnum permissionName);
}