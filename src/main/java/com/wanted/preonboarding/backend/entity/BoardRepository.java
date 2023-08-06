package com.wanted.preonboarding.backend.entity;

import com.wanted.preonboarding.backend.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT new com.wanted.preonboarding.backend.dto.BoardDto(b.id, b.title, b.description, m.username) from Board b JOIN Member m ON b.member = m ")
    Page<BoardDto> findBoardDtoAll(Pageable pageable);

    Optional<Board> findBoardById(Long id);
}
