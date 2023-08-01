package com.wanted.preonboarding.backend.service;

import com.wanted.preonboarding.backend.annotation.LoginCheck;
import com.wanted.preonboarding.backend.entity.Board;
import com.wanted.preonboarding.backend.entity.BoardRepository;
import com.wanted.preonboarding.backend.entity.Member;
import com.wanted.preonboarding.backend.utils.ContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RemoveBoardService {

    private final BoardRepository boardRepository;
    private final FindBoardService findBoardService;

    @LoginCheck
    @Transactional
    public ResponseEntity<?> removeBoard(Long boardId) {
        Member session = ContextHolder.getSession();
        Board targetBoard = findBoardService.findBoard(boardId);

        targetBoard.authenticationCheck(session);
        boardRepository.delete(targetBoard);

        return ResponseEntity.accepted().body(null);
    }

}
