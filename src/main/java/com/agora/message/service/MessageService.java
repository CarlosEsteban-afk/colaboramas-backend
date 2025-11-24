package com.agora.message.service;

import com.agora.message.dto.MessageRequestDto;
import com.agora.message.dto.MessageResponseDto;
import com.agora.message.model.Message;
import com.agora.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repo;

    public MessageResponseDto sendMessage(MessageRequestDto dto) {

        Message m = new Message();
        m.setFromUserId(dto.getFromUserId());
        m.setToUserId(dto.getToUserId());
        m.setSubject(dto.getSubject());
        m.setBody(dto.getMessage());
        m.setStatus("pending");
        m.setCreatedAt(Instant.now());
        m.setUpdatedAt(Instant.now());

        Message saved = repo.save(m);
        return toResponseDto(saved);
    }


    public List<MessageResponseDto> getReceived(Long userId) {
        return repo.findByToUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponseDto).toList();
    }


    public List<MessageResponseDto> getSent(Long userId) {
        return repo.findByFromUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponseDto).toList();
    }


    public MessageResponseDto respond(Long id, String status) {

        if (!status.equals("accepted") && !status.equals("rejected") && !status.equals("pending")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        Message m = repo.findById(id).orElse(null);

        m.setStatus(status);
        m.setUpdatedAt(Instant.now());

        Message saved = repo.save(m);
        return toResponseDto(saved);
    }


    private MessageResponseDto toResponseDto(Message m) {
        MessageResponseDto r = new MessageResponseDto();
        r.setId(m.getId());
        r.setFromUserId(m.getFromUserId());
        r.setToUserId(m.getToUserId());
        r.setSubject(m.getSubject());
        r.setMessage(m.getBody());
        r.setStatus(m.getStatus());
        r.setCreatedAt(m.getCreatedAt());
        return r;
    }
}
