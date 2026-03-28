package com.agora.auth.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"email", "message", "token", "status"})
public record AuthResponse(String email, String message, String token, boolean status) {}
