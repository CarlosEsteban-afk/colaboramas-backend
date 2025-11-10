package com.agora.user.repository;

import com.agora.user.model.UserKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserKeywordRepository extends JpaRepository<UserKeyword, Long> {
}
