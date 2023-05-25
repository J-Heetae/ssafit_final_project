package com.ssafy.ssafit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.Likes;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.QLikes;
import com.ssafy.ssafit.dto.LikesDTO;
import com.ssafy.ssafit.repository.BoardRepository;
import com.ssafy.ssafit.repository.LikesRepository;
import com.ssafy.ssafit.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

	private final BoardRepository boardRepository;
	private final LikesRepository likesRepository;
	private final MemberRepository memberRepository;
	private final JPAQueryFactory factory;

	private QLikes likes = QLikes.likes;

	@Override
	public Long selectLikeCount(Board board) {
		return likesRepository.countByBoard(board);
	}

	@Override
	public List<Likes> selectLikesListByUserId(String userId) {
		List<Likes> likesList = factory.selectFrom(likes).where(likes.member.memberId.eq(userId))
				.orderBy(likes.board.boardNo.desc()).fetch();

		return likesList;
	}

	@Override
	public Long likes(LikesDTO likes) {
		Board savedBoard = boardRepository.findById(likes.getBoardNo()).get();
		Member savedMember = memberRepository.findById(likes.getMemberId()).get();

		Likes savedLikes = likesRepository.findByboardAndMember(savedBoard, savedMember);

		if (savedLikes == null)
			likesRepository.save(new Likes(0L, savedBoard, savedMember));
		else
			likesRepository.delete(savedLikes);

		return likesRepository.countByBoard(savedBoard);
	}

}
