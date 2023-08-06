package com.wanted.preonboarding.backend.dto;

import com.wanted.preonboarding.backend.entity.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SignupDto {
    @NotNull(message = "이메일 값을 넣어주세요. @가 포함되어야 합니다.")
    @Pattern(regexp = "[^\s]*@[^\s]*$", message = "@가 포함되어야 합니다.")
    private String username;

    @NotNull(message = "값을 넣어주세요. 8글자 이상이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9^\s]{8,}", message = "8글자 이상이어야 합니다.")
    private String password;


    public Member toEntity() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);

        return Member.builder()
                .username(username)
                .password(encryptedPassword)
                .build();
    }
}
