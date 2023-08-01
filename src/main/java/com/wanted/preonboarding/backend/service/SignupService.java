package com.wanted.preonboarding.backend.service;

import com.wanted.preonboarding.backend.dto.SignupDto;
import com.wanted.preonboarding.backend.entity.Member;
import com.wanted.preonboarding.backend.entity.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity<?> signUp(SignupDto dto) {
        Member newMember = dto.toEntity();
        memberRepository.save(newMember);
        return ResponseEntity.accepted().body(null);
    }
}
