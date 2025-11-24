package com.agora.message.repository;

import com.agora.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByToUserIdOrderByCreatedAtDesc(Long toUserId);

    List<Message> findByFromUserIdOrderByCreatedAtDesc(Long fromUserId);
}
