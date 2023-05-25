package com.ssafy.ssafit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.ssafit.domain.VideoComment;
import com.ssafy.ssafit.dto.VideoCommentDto;
import com.ssafy.ssafit.repository.VideoRepository;
import com.ssafy.ssafit.service.MemberService;
import com.ssafy.ssafit.service.VideoCommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videoComment")
public class VideoCommentController {
	
	private final VideoCommentService videoCommentService;
	private final VideoRepository videoRepository;
	private final MemberService memberService;
	
	/**
	 * 해당 영상의 모든 댓글 조회하기
	 */
	@GetMapping
	public ResponseEntity<List<VideoCommentDto>> findVideoComments(@RequestParam("no") Long videoNo) {
		List<VideoCommentDto> list = videoCommentService.findAllComments(videoNo);
		System.out.println(list.size());
		return new ResponseEntity<List<VideoCommentDto>>(list, HttpStatus.OK);
	}
	
	/**
	 * 댓글 삭제
	 */
	@DeleteMapping("/{no}")
	public ResponseEntity<Void> deleteVideoComment(@PathVariable("no") Long no) {
		videoCommentService.delete(no);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	/**
	 * 댓글  추가
	 */
	@PostMapping
	public ResponseEntity<VideoCommentDto> addComment(
			@RequestParam("comment") String content,
			@RequestParam("memberId") String memberId,
			@RequestParam("videoNo") Long videoNo) {
		VideoComment videoComment = new VideoComment();
		
		videoComment.setContent(content);
//		videoComment.setTitle("");
		videoComment.setVideo(videoRepository.findById(1L).get());
		videoComment.setMember(memberService.findByMemberId("user1"));
		
		VideoCommentDto result = videoCommentService.insert(videoComment);
		
		System.out.println(result.toString());
		
		return new ResponseEntity<VideoCommentDto>(result, HttpStatus.OK);
	}
}