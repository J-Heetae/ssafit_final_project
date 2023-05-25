package com.ssafy.ssafit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.BoardType;
import com.ssafy.ssafit.domain.File;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.dto.BoardDTO;
import com.ssafy.ssafit.dto.MemberDTO;

public interface BoardService {

	/**
	 * 게시글 등록
	 */
	BoardDTO insert(BoardDTO board, File file);

	/**
	 * 게시글 수정 : 제목, 내용 수정 가능
	 */
	BoardDTO update(BoardDTO board);

	/**
	 * 게시글 삭제
	 */
	void delete(Long boardId);

	/**
	 * 게시글 전체 조회 : 게시판별, 등록일 순, 조회수 순
	 */
	Page<Board> findAll(Pageable pageable, BoardType boardType, String orderCondition, String orderDirection);

	/**
	 * 게시글 상세 조회
	 */
	BoardDTO findByBoardId(Long boardId);

	// DTO 변환
	BoardDTO getBoardDTO(Board savedBoard);

	List<BoardDTO> findAllBoard(BoardType boardType, String orderCondition, String orderDirection);

}
