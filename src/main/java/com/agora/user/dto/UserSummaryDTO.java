package com.agora.user.dto;

import lombok.Data;

@Data
public class UserSummaryDTO {
    private Long id;
    private String username;
    private String email;
    private String imageUrl;

    public UserSummaryDTO(Long id, String username, String email, String imageUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
