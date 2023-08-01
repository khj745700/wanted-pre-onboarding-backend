package com.wanted.preonboarding.backend.controller;

import com.wanted.preonboarding.backend.annotation.LoginCheck;
import com.wanted.preonboarding.backend.dto.BoardDto;
import com.wanted.preonboarding.backend.dto.BoardRegisterDto;
import com.wanted.preonboarding.backend.service.EditBoardService;
import com.wanted.preonboarding.backend.service.FindBoardService;
import com.wanted.preonboarding.backend.service.RegisterBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("")
    public ResponseEntity<?> boardRegister(@Valid @RequestBody BoardRegisterDto dto) {
        return registerBoardService.registerBoard(dto);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> boardEdit(@Valid @RequestBody BoardDto dto, @PathVariable Long boardId) {
        return editBoardService.editBoard(dto, boardId);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> boardDetailFind(@PathVariable Long boardId) {
        return findBoardService.findBoardDetails(boardId);
    }
}
