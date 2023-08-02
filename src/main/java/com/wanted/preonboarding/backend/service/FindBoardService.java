package com.wanted.preonboarding.backend.service;


import com.wanted.preonboarding.backend.dto.BoardDto;
import com.wanted.preonboarding.backend.entity.Board;
import com.wanted.preonboarding.backend.entity.BoardRepository;
import com.wanted.preonboarding.backend.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindBoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<?> findBoardDetails(Long boardId) {
        BoardDto dto = BoardDto.of(findBoard(boardId));
        return ResponseEntity.ok(dto);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<?> findBoards(Pageable pageable) {
        Page<BoardDto> boards = boardRepository.findBoardDtoAll(pageable);

        return ResponseEntity.ok(boards);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Board findBoard(Long boardId) {
        return boardRepository.findBoardById(boardId).orElseThrow(NotFoundException::new);
    }
}
