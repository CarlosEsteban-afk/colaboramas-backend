package com.gituhub.api_service.controller.dto;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record AuthCreateRoleRequest(@Size(max = 1, message = "the user cannot have more than 1 role") List<String> roleListName) {}
