package com.ssafy.ssafit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.BoardComment;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

	List<BoardComment> findByBoard(Board board);
	
}
