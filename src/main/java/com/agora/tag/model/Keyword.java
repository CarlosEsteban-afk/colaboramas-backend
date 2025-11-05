package com.agora.tag.model;

import com.agora.user.model.UserKeyword;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "keywords")
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserKeyword> userLinks = new HashSet<>();
}
