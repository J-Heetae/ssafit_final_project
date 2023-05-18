package com.ssafy.ssafit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.dto.JoinCntByMonthDto;

public interface MemberRepository extends JpaRepository<Member, String>{
	
	@Query("SELECT MONTH(m.regDate) as month, COUNT(m) as joinCnt "
			+ "FROM Member m "
			+ "WHERE YEAR(m.regDate) = :year "
			+ "GROUP BY MONTH(m.regDate)")
	List<JoinCntByMonthDto> countMembersByMonth(@Param("year") int year);	

	Member findByMemberIdAndPassword(@Param("memberId") String memberId, @Param("password") String password);

}
