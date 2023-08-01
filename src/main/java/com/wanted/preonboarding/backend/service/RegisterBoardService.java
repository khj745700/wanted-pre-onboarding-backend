package com.wanted.preonboarding.backend.service;


import com.wanted.preonboarding.backend.annotation.LoginCheck;
import com.wanted.preonboarding.backend.dto.BoardRegisterDto;
import com.wanted.preonboarding.backend.entity.Board;
import com.wanted.preonboarding.backend.entity.BoardRepository;
import com.wanted.preonboarding.backend.entity.Member;
import com.wanted.preonboarding.backend.utils.ContextHolder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterBoardService {

    private final BoardRepository boardRepository;

    @LoginCheck
    @Transactional
    public ResponseEntity<?> registerBoard(BoardRegisterDto dto) {
        Member nowSession = ContextHolder.getSession();
        Board newBoard = dto.toEntity(nowSession);
        boardRepository.save(newBoard);
        return ResponseEntity.ok(null);
    }
}
