package com.agora.tag.repository;

import com.agora.tag.model.Keyword;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByName(String name);
    
} 