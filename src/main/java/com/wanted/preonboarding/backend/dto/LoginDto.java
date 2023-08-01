package com.wanted.preonboarding.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginDto {
    @NotNull(message = "이메일 값을 넣어주세요. @가 포함되어야 합니다.")
    @Pattern(regexp = "[^\s]*@[^\s]*$")
    private String username;

    @NotNull(message = "값을 넣어주세요. 8글자 이상이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9^\s]{8,}")
    private String password;
}
