package com.wanted.preonboarding.backend.controller;

import com.wanted.preonboarding.backend.dto.SignupDto;
import com.wanted.preonboarding.backend.service.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;

    @PostMapping("signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto dto) {
        return signupService.signUp(dto);
    }
}
