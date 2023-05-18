package com.ssafy.ssafit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssafy.ssafit.domain.Member;

public interface MemberService {

	// 회원가입
	Member join(Member member);

	// 회원 탈퇴
	void delete(String memberId);

	// 회원 정보 수정 - 비밀번호, 이름, 연락처
	Member update(Member member);

	// 회원 정보 상세 조회
	Member login(Member member);

	// 전체 회원 조회
//	   - 리뷰를 많이 단 순
//	   - 가입일 순
//	   - 가나다 순
	Page<Member> findAll(Pageable pageable, String orderCondition, String orderDirection);

}
