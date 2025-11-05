
package com.agora.user.repository;

import com.agora.auth.model.Role;
import com.agora.auth.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findRolesByRoleNameIn(List<RoleEnum> roleNames);

    Optional<Role> findByRoleName(RoleEnum roleName);
}
