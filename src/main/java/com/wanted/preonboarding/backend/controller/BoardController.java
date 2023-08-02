package com.wanted.preonboarding.backend.controller;

import com.wanted.preonboarding.backend.annotation.LoginCheck;
import com.wanted.preonboarding.backend.dto.BoardDto;
import com.wanted.preonboarding.backend.dto.BoardRegisterDto;
import com.wanted.preonboarding.backend.service.EditBoardService;
import com.wanted.preonboarding.backend.service.FindBoardService;
import com.wanted.preonboarding.backend.service.RegisterBoardService;
import com.wanted.preonboarding.backend.service.RemoveBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final RegisterBoardService registerBoardService;
    private final EditBoardService editBoardService;
    private final FindBoardService findBoardService;
    private final RemoveBoardService removeBoardService;


    @GetMapping("/{boardId}")
    public ResponseEntity<?> boardDetailFind(@PathVariable Long boardId) {
        return findBoardService.findBoardDetails(boardId);
    }

    @GetMapping("")
    public ResponseEntity<?> boardLists(Pageable pageable) {
        return findBoardService.findBoards(pageable);
    }

    @PostMapping("")
    public ResponseEntity<?> boardRegister(@Valid @RequestBody BoardRegisterDto dto) {
        return registerBoardService.registerBoard(dto);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> boardEdit(@Valid @RequestBody BoardDto dto, @PathVariable Long boardId) {
        return editBoardService.editBoard(dto, boardId);
    }


    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> boardRemove(@PathVariable Long boardId) {
        return removeBoardService.removeBoard(boardId);
    }
}
