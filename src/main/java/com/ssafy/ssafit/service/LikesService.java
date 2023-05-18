package com.ssafy.ssafit.service;

import java.util.List;

import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.Likes;

public interface LikesService {

	// 게시글 좋아요
	Likes insert(Likes likes);

	// 게시글 좋아요 취소
	void delete(Long likeNo);

	// 좋아요 개수 조회
	Long selectLikeCount(Board board);

	// 회원이 좋아요를 누른 리스트
	List<Likes> selectLikesListByUserId(String userId);
}
