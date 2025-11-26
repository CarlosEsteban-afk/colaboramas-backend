package com.agora.admin.repository;

import com.agora.admin.model.AdminStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminStatsRepository extends JpaRepository<AdminStats, Long> {
    Optional<AdminStats> findTopByOrderByCreatedAtDesc();
}
