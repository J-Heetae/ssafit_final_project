package com.ssafy.ssafit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ssafy.ssafit.domain.BoardComment;
import com.ssafy.ssafit.exception.NotFoundException;
import com.ssafy.ssafit.repository.BoardCommentRepository;
import com.ssafy.ssafit.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardCommentServiceImpl implements BoardCommentService {

	private final BoardCommentRepository commentRepository;
	private final BoardRepository boardRepository;

	@Override
	public BoardComment insert(BoardComment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public BoardComment update(BoardComment comment) {
		BoardComment savedComment = commentRepository.findById(comment.getComment_no()).orElse(null);

		if (savedComment == null)
			throw new NotFoundException("등록된 댓글을 찾을 수 없습니다.");

		// 제목 및 내용 변경
		savedComment.setTitle(comment.getTitle());
		savedComment.setContent(comment.getContent());

		// 변경된 엔티티 리턴
		return savedComment;
	}

	@Override
	public void delete(Long commentId) {
		commentRepository.deleteById(commentId);
	}

	@Override
	public List<BoardComment> findAllComments(Long boardId) {
		List<BoardComment> comments = commentRepository.findByBoard(boardRepository.findById(boardId).get());
		return comments;
	}

}
