package com.agora.user.model;

import com.agora.tag.model.Keyword;
import com.agora.tag.model.KeywordType;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_keywords", uniqueConstraints = { 
        @UniqueConstraint(columnNames = { "user_id", "keyword_id", "keyword_type" })
})
public class UserKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @Enumerated(EnumType.STRING)
    @Column(name = "keyword_type", nullable = false)
    private KeywordType type;
}
