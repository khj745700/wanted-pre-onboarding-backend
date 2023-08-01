package com.wanted.preonboarding.backend.service;

import com.wanted.preonboarding.backend.annotation.LoginCheck;
import com.wanted.preonboarding.backend.dto.BoardDto;
import com.wanted.preonboarding.backend.entity.Board;
import com.wanted.preonboarding.backend.entity.BoardRepository;
import com.wanted.preonboarding.backend.entity.Member;
import com.wanted.preonboarding.backend.exception.NotFoundException;
import com.wanted.preonboarding.backend.utils.ContextHolder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditBoardService {

    private final FindBoardService findBoardService;
    @LoginCheck
    @Transactional
    public ResponseEntity<?> editBoard(BoardDto dto, Long boardId){
        Member session = ContextHolder.getSession();

        Board targetBoard = findBoardService.findBoard(boardId);
        targetBoard.authenticationCheck(session);
        targetBoard.update(dto);

        return ResponseEntity.ok(null);
    }
}
