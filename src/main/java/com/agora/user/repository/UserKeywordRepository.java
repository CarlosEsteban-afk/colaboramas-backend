package com.agora.user.repository;

import com.agora.user.model.UserKeyword;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserKeywordRepository extends JpaRepository<UserKeyword, Long> {
    @Query("SELECT uk FROM UserKeyword uk JOIN FETCH uk.keyword WHERE uk.user.id IN :userIds")
    List<UserKeyword> findByUserIdInWithDetails(@Param("userIds") List<Long> userIds);
}
