package com.agora.admin.repository;

import com.agora.admin.model.CountryCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryCountRepository extends JpaRepository<CountryCount, Long> {
}
