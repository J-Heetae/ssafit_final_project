package com.ssafy.ssafit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.ssafy.ssafit.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

	Member findByMemberIdAndPassword(@Param("memberId") String memberId, @Param("password") String password);
}
