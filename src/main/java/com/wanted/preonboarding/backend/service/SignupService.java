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
        //원래는 이메일 중복 가입 확인 해야 하나, 나머지 유효성 검사는 하지말라 하였으므로....
        if(memberRepository.existsMemberByUsername(newMember.getUsername())){
            return ResponseEntity.badRequest().body(null);
        }

        memberRepository.save(newMember);
        return ResponseEntity.accepted().body(null);
    }
}
