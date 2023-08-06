package com.wanted.preonboarding.backend.dto;

import com.wanted.preonboarding.backend.entity.Board;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BoardDto {
    private Long boardId;
    @NotNull
    private String title;

    @NotNull
    private String description;

    private String username;

    public static BoardDto of(final Board board){
        BoardDto boardDto = BoardDto.builder()
                .description(board.getDescription())
                .title(board.getTitle())
                .username(board.getMember().getUsername())
                .boardId(board.getId())
                .build();
        return boardDto;
    }
}
