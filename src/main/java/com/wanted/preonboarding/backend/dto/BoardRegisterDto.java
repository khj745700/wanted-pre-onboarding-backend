package com.wanted.preonboarding.backend.dto;

import com.wanted.preonboarding.backend.entity.Board;
import com.wanted.preonboarding.backend.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BoardRegisterDto {
    @NotNull
    private String title;

    @NotNull
    private String description;

    public Board toEntity(Member member) {
        return Board.builder()
                .member(member)
                .title(title)
                .description(description)
                .build();
    }
}
