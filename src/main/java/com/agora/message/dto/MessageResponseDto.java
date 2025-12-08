package com.agora.message.dto;
import com.agora.user.dto.UserSummaryDTO;
import lombok.Data;

import java.time.Instant;
@Data
public class MessageResponseDto {

    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String subject;
    private String message;
    private String status;
    private Instant createdAt;
    private UserSummaryDTO fromUser;
    private UserSummaryDTO toUser;

    // GETTERS & SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFromUserId() { return fromUserId; }
    public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }

    public Long getToUserId() { return toUserId; }
    public void setToUserId(Long toUserId) { this.toUserId = toUserId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
