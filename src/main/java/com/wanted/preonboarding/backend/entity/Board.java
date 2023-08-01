package com.wanted.preonboarding.backend.entity;

import com.wanted.preonboarding.backend.dto.BoardDto;
import com.wanted.preonboarding.backend.service.EditAuthenticationCheck;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@NoArgsConstructor
@SuperBuilder
@Getter
public class Board extends BaseEntity implements EditAuthenticationCheck {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String title;

    public void update(BoardDto dto) {
        this.description = dto.getDescription();
        this.title = dto.getTitle();
    }
}
