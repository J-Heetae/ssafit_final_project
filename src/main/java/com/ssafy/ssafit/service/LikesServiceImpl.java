package com.ssafy.ssafit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.Likes;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.QLikes;
import com.ssafy.ssafit.repository.LikesRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

	private final LikesRepository repository;
	private final JPAQueryFactory factory;

	private QLikes likes = QLikes.likes;

	@Override
	public Likes insert(Likes like) {
		return repository.save(like);
	}

	@Override
	public void delete(Long likeNo) {
		repository.deleteById(likeNo);
	}

	@Override
	public Long selectLikeCount(Board board) {
		return repository.countByBoard(board);
	}

	@Override
	public List<Likes> selectLikesListByUserId(String userId) {
		// 수정 요망
		Member member = null;

		List<Likes> likesList = factory.selectFrom(likes).where(likes.member.memberId.eq(member.getMemberId()))
				.orderBy(likes.board.boardNo.desc()).fetch();

		return likesList;
	}

}
