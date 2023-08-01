package com.wanted.preonboarding.backend.service;


import com.wanted.preonboarding.backend.entity.Board;
import com.wanted.preonboarding.backend.entity.BoardRepository;
import com.wanted.preonboarding.backend.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindBoardService {

    private final BoardRepository boardRepository;


    @Transactional(readOnly = true)
    public ResponseEntity<?> findBoardDetails(Long boardId) {
        return ResponseEntity.ok(findBoard(boardId));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Board findBoard(Long boardId) {
        return boardRepository.findBoardById(boardId).orElseThrow(NotFoundException::new);
    }
}
