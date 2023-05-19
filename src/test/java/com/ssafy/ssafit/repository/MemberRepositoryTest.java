package com.ssafy.ssafit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.dto.JoinCntByMonthDto;


@SpringBootTest
@Transactional
public class MemberRepositoryTest {
	
	@Autowired
	MemberRepository memberRepository;

	@Test
	public void 월별회원가입수() throws Exception {
		//Given
        for (int i = 1; i <= 5; i++) {
            Member member = new Member();
            member.setMemberId("member" + i);
            member.setPassword("password" + i);
            member.setName("Name" + i);
            member.setAge(i * 10);
            member.setEmail("email" + i + "@example.com");
            member.setRegDate(LocalDateTime.now());

            memberRepository.save(member);
        }
		//When
        List<JoinCntByMonthDto> findMembers = memberRepository.countMembersByMonth(2023);
		
        for(JoinCntByMonthDto member : findMembers) {
        	System.out.println(member.getJoinCnt());
        	System.out.println(member.getMonth());
        }
        
		//Then
        assertThat(findMembers.size()).isEqualTo(1);
	}
}
