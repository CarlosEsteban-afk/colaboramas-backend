package com.agora.profile.dto;

import com.agora.tag.model.KeywordType;
import lombok.Data;

@Data
public class KeywordDto {
    private String name;
    private KeywordType type;
}
