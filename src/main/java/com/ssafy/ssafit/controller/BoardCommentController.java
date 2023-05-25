package com.ssafy.ssafit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.ssafit.dto.BoardCommentDTO;
import com.ssafy.ssafit.service.BoardCommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardCommentController {

	private final BoardCommentService boardCommentService;
	
	@PostMapping("/boardComment")
	public ResponseEntity<BoardCommentDTO> insertComment(BoardCommentDTO comment) {
		BoardCommentDTO savedComment = boardCommentService.insert(comment);
		return new ResponseEntity<BoardCommentDTO>(savedComment, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/boardComment/{commentNo}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentNo) {
		boardCommentService.delete(commentNo);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/boardComment/{boardNo}")
	public ResponseEntity<List<BoardCommentDTO>> getCommentList(@PathVariable Long boardNo) {
		List<BoardCommentDTO> comments= boardCommentService.findAllComments(boardNo);
		
		return new ResponseEntity<List<BoardCommentDTO> >(comments, HttpStatus.ACCEPTED);
	}
}
