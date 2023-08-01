package com.wanted.preonboarding.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BoardDto {
    @NotNull
    private String title;

    @NotNull
    private String description;
}
