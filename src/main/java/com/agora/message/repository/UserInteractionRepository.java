package com.agora.message.repository;

import com.agora.message.model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {
    List<UserInteraction> findByFromUserId(Long fromUserId);
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
