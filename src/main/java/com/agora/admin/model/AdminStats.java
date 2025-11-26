package com.agora.admin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_stats")
public class AdminStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_users")
    private Long totalUsers;

    @Column(name = "investigators_count")
    private Long investigatorsCount;

    @Column(name = "communicators_count")
    private Long communicatorsCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AdminStats() {}

    public AdminStats(Long totalUsers, Long investigatorsCount, Long communicatorsCount) {
        this.totalUsers = totalUsers;
        this.investigatorsCount = investigatorsCount;
        this.communicatorsCount = communicatorsCount;
        this.createdAt = LocalDateTime.now();
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
    public Long getInvestigatorsCount() { return investigatorsCount; }
    public void setInvestigatorsCount(Long investigatorsCount) { this.investigatorsCount = investigatorsCount; }
    public Long getCommunicatorsCount() { return communicatorsCount; }
    public void setCommunicatorsCount(Long communicatorsCount) { this.communicatorsCount = communicatorsCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
