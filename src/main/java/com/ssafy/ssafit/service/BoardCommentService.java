package com.ssafy.ssafit.service;

import java.util.List;

import com.ssafy.ssafit.domain.BoardComment;
import com.ssafy.ssafit.dto.BoardCommentDTO;

public interface BoardCommentService {
	// 댓글 등록
	BoardCommentDTO insert(BoardCommentDTO comment);

	// 댓글 수정
	BoardComment update(BoardComment comment);

	// 댓글 삭제
	void delete(Long commentId);

	// 댓글 조회
	List<BoardCommentDTO> findAllComments(Long boardId);

}
