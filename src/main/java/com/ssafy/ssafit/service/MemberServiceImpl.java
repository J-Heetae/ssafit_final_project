package com.ssafy.ssafit.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.QComment;
import com.ssafy.ssafit.domain.QMember;
import com.ssafy.ssafit.domain.asset.OrderDirection;
import com.ssafy.ssafit.exception.DuplicatedException;
import com.ssafy.ssafit.exception.NotFoundException;
import com.ssafy.ssafit.repository.MemberRepository;
import com.ssafy.ssafit.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final JPAQueryFactory queryFactory;

	private QMember member = QMember.member;
	// private QComment comment = QComment.comment;

	// jwt 인증을 위한 필드
	@Value("${jwt.secret}")
	private String secretKey;
	private Long expiredMs = 1000 * 60 * 60L;

	@Override
	public Member join(Member member) {
		memberRepository.findById(member.getMemberId()).ifPresent(mem -> {
			throw new DuplicatedException("중복된 아이디입니다.");
		});

		return memberRepository.save(member);
	}

	@Override
	public void delete(String memberId) {
		memberRepository.findById(memberId).orElseThrow(() -> {
			throw new NotFoundException("이미 존재하지 않는 회원입니다.");
		});

		memberRepository.deleteById(memberId);
	}

	// 회원 정보 수정 - 비밀번호, 이름
	@Override
	public Member update(Member member) {
		Member savedMember = memberRepository.findById(member.getMemberId()).orElseThrow(() -> {
			throw new NotFoundException("존재하지 않는 회원입니다.");
		});

		savedMember.setPassword(member.getPassword());
		savedMember.setName(member.getName());
		savedMember.setEmail(member.getEmail());

		return savedMember;
	}

	@Override
	public String login(Member member) {
		Member savedMember = memberRepository.findByMemberIdAndPassword(member.getMemberId(), member.getPassword());

		if (savedMember == null)
			throw new NotFoundException("아이디 또는 비밀번호 오류입니다.");

		return this.createToken(savedMember.getMemberId());

	}

	private String createToken(String memberId) {
		return JWTUtil.createToken(memberId, secretKey, expiredMs);
	}

	@Override
	public Member findMember(Member member) {
		return memberRepository.findById(member.getMemberId()).orElseThrow(() -> {
			throw new NotFoundException("회원 정보를 조회할 수 없습니다.");
		});
	}
	
	@Override
	public Page<Member> findAll(Pageable pageable, String orderCondition, String orderDirection) {

		List<Member> members = queryFactory.selectFrom(member)
				.orderBy(getOrderSpecifier(orderCondition, orderDirection).stream().toArray(OrderSpecifier[]::new))
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

		JPAQuery<Long> countQuery = queryFactory.select(member.count()).from(member);

		return PageableExecutionUtils.getPage(members, pageable, countQuery::fetchOne);
	}

	private Sort sortByOrderCondition(String orderCondition, String orderDirection) {
		// 내림차순 기본
		Direction direction = Direction.DESC;

		// 오름차순 조건일 때, 오름차순으로 변경
		if (orderDirection.toUpperCase().equals(OrderDirection.ASC))
			direction = Direction.ASC;

		Sort sort = null;

		switch (orderCondition) {
		case "name":
			sort = Sort.by(direction, "name");
		case "regDate":
			sort = Sort.by(direction, "regDate");
		default:
			sort = Sort.by(direction, "memberId");
		}

		return sort;
	}

	private List<OrderSpecifier> getOrderSpecifier(String orderCondition, String orderDirection) {
		List<OrderSpecifier> orderBy = new ArrayList<OrderSpecifier>();

		Sort sort = null;
		if (orderCondition.equals("comment"))
			orderBy.add(member.comments.size().desc());
		else
			sort = sortByOrderCondition(orderCondition, orderDirection);

		if (sort != null) {
			sort.stream().forEach(order -> {
				Order direction = order.isDescending() ? Order.DESC : Order.ASC;
				// 정렬 기준 컬럼
				String prop = order.getProperty();
				// QBoard
				PathBuilder<Member> pathBuilder = new PathBuilder<Member>(Member.class, "member");

				// direction과 필드 path 넣어줌
				orderBy.add(new OrderSpecifier(direction, pathBuilder.get(prop)));

			});
		}
		return orderBy;
	}

}
