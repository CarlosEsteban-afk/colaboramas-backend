package com.agora.profile.repository;

import com.agora.profile.model.Educacion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EducacionRepository extends JpaRepository<Educacion, Long> {
   
    @Query("SELECT e FROM Educacion e WHERE e.user.id IN :userIds " +
           "AND e.id = (SELECT MIN(e2.id) FROM Educacion e2 WHERE e2.user.id = e.user.id)")
    List<Educacion> findFirstByUserIdIn(@Param("userIds") List<Long> userIds);
}
