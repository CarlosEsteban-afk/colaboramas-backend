package com.gituhub.api_service.persistence.repository;

import com.gituhub.api_service.persistence.entity.RoleEntity;
import com.gituhub.api_service.persistence.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    List<RoleEntity> findRolesByRoleNameIn(List<RoleEnum> roleNames);
}
