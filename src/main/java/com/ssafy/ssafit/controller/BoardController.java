package com.ssafy.ssafit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssafy.ssafit.domain.BoardType;
import com.ssafy.ssafit.dto.BoardDTO;
import com.ssafy.ssafit.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

	private final BoardService boardService;

	@GetMapping("/board")
	public ResponseEntity<List<BoardDTO>> findAll(@RequestParam(required = false) BoardType boardType,
			String orderCondition, @RequestParam(defaultValue = "ASC") String orderDirection) {

		// Pageable pageable = PageRequest.of((nowPage -1), PAGE_COUNT);
		List<BoardDTO> boardList = boardService.findAllBoard(boardType, orderCondition, orderDirection);

		return new ResponseEntity<List<BoardDTO>>(boardList, HttpStatus.OK);
	}

	@GetMapping("/board/{id}")
	public ResponseEntity<BoardDTO> findBoard(@PathVariable("id") Long boardNo) {
		BoardDTO board = boardService.findByBoardId(boardNo);
		return new ResponseEntity<BoardDTO>(board, HttpStatus.OK);
	}

}
