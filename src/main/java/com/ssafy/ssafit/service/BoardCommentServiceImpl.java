package com.ssafy.ssafit.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.BoardComment;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.dto.BoardCommentDTO;
import com.ssafy.ssafit.exception.NotFoundException;
import com.ssafy.ssafit.repository.BoardCommentRepository;
import com.ssafy.ssafit.repository.BoardRepository;
import com.ssafy.ssafit.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardCommentServiceImpl implements BoardCommentService {

	private final BoardCommentRepository commentRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;

	@Override
	public BoardCommentDTO insert(BoardCommentDTO comment) {
		Member savedMember = memberRepository.findById(comment.getMemberId()).get();
		Board savedBoard = boardRepository.findById(comment.getBoardNo()).get();

		BoardComment entity = new BoardComment(0L, savedMember, comment.getContent(), null, null, savedBoard);
		BoardComment savedComment = commentRepository.save(entity);
		return BoardCommentDTO.builder().boardNo(savedBoard.getBoardNo()).commentNo(savedComment.getCommentNo())
				.content(savedComment.getContent()).memberId(savedMember.getMemberId()).build();
	}

	@Override
	public BoardComment update(BoardComment comment) {
		BoardComment savedComment = commentRepository.findById(comment.getCommentNo()).orElse(null);

		if (savedComment == null)
			throw new NotFoundException("등록된 댓글을 찾을 수 없습니다.");

		// 제목 및 내용 변경
		savedComment.setContent(comment.getContent());

		// 변경된 엔티티 리턴
		return savedComment;
	}

	@Override
	public void delete(Long commentId) {
		commentRepository.deleteById(commentId);
	}

	@Override
	public List<BoardCommentDTO> findAllComments(Long boardId) {
		List<BoardComment> comments = commentRepository.findByBoard(boardRepository.findById(boardId).get());

		List<BoardCommentDTO> result = comments.stream()
				.map(savedComment -> BoardCommentDTO.builder().boardNo(savedComment.getBoard().getBoardNo())
						.commentNo(savedComment.getCommentNo()).content(savedComment.getContent())
						.memberId(savedComment.getMember().getMemberId()).build())
				.collect(Collectors.toList());

		return result;

	}

}
