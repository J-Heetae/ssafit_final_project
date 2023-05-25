package com.ssafy.ssafit.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.dto.MemberDTO;
import com.ssafy.ssafit.repository.MemberRepository;
import com.ssafy.ssafit.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	
	@PostMapping("/member/login")
	public ResponseEntity<Map<String, Object>> login(Member member) {

		Map<String, Object> result = new HashMap<String, Object>();

		// 로그인 유저 확인
		String token = memberService.login(member);
		
		// 저장된 유저 정보
		Member savedMember = memberService.findMember(member);
		
		// DTO로 변경 - 비밀번호 제외
		MemberDTO savedMem = MemberDTO.builder()
				.memberId(savedMember.getMemberId())
				.name(savedMember.getName())
				.age(savedMember.getAge())
				.email(savedMember.getEmail())
				.regDate(savedMember.getRegDate())
				.build();

		
		result.put("access-token", token);
		result.put("loginMember", savedMem);

		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.ACCEPTED);
	}

	@PostMapping("/member")
	public ResponseEntity<String> join(Member member) {

		ResponseEntity<String> result = null;

		Member savedMember = memberService.join(member);

		if (savedMember != null)
			result = new ResponseEntity<String>("가입이 완료되었습니다.", HttpStatus.ACCEPTED);
		else
			result = new ResponseEntity<String>("가입 실패입니다.", HttpStatus.NOT_ACCEPTABLE);

		return result;
	}

	/**
	 * 회원정보 수정
	 * @param member
	 * @return
	 */
	@PutMapping("/member")
	public ResponseEntity<MemberDTO> update(Member member) {
		Member updatedmember = memberService.update(member);
		// 비밀번호 정보 없이 리턴
		MemberDTO result = MemberDTO.builder()
				.memberId(updatedmember.getMemberId())
				.name(updatedmember.getName())
				.age(updatedmember.getAge())
				.email(updatedmember.getEmail())
				.regDate(updatedmember.getRegDate())
				.build();

		return new ResponseEntity<MemberDTO>(result, HttpStatus.ACCEPTED);
	}
	
	/**
	 * 회원정보 삭제
	 * @param memberId
	 * @return
	 */
	@DeleteMapping("/member/{memberId}")
	public ResponseEntity<String> delete(String memberId) {
		memberService.delete(memberId);
		return new ResponseEntity<String>("회원 탈퇴가 완료되었습니다.", HttpStatus.ACCEPTED);
	}
	
}
