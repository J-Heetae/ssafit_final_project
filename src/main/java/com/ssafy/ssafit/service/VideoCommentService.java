package com.ssafy.ssafit.service;

import java.util.List;

import com.ssafy.ssafit.domain.VideoComment;
import com.ssafy.ssafit.dto.VideoCommentDto;
import com.ssafy.ssafit.dto.VideoDto;

public interface VideoCommentService {
	// 댓글 등록
	VideoCommentDto insert(VideoComment comment);

	// 댓글 수정
	VideoComment update(VideoComment comment);

	// 댓글 삭제
	void delete(Long commentId);

	// 해당 영상의 모든 댓글 조회
	List<VideoCommentDto> findAllComments(Long videoId);

	// 댓글 번호로 댓글 조회
	VideoCommentDto findByNo(Long commentNo);

}
