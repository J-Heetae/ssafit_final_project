package com.ssafy.ssafit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.Likes;
import com.ssafy.ssafit.dto.BoardDTO;
import com.ssafy.ssafit.dto.LikesDTO;
import com.ssafy.ssafit.service.BoardService;
import com.ssafy.ssafit.service.LikesService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikesController {

	private final BoardService boardService;
	private final LikesService likesService;

	@PostMapping("/likes")
	public ResponseEntity<Long> likes(LikesDTO likes) {
		Long result = likesService.likes(likes);
		return new ResponseEntity<Long>(result, HttpStatus.OK);
	}

	public ResponseEntity<List<BoardDTO>> selectLikesListByUserId(String memberId) {

		List<Likes> likesList = likesService.selectLikesListByUserId(memberId);

		List<BoardDTO> result = new ArrayList<BoardDTO>();
		for (Likes likes : likesList) {
			Board savedBoard = likes.getBoard();
			result.add(boardService.getBoardDTO(savedBoard));
		}
		return new ResponseEntity<List<BoardDTO>>(result, HttpStatus.OK);
	}
}
