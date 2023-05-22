package com.ssafy.ssafit.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	private static final String SUCCESS = "succes";
	private static final String FAIL = "fail";

	@PostMapping("/member/login")
	public ResponseEntity<Map<String, Object>> login(Member member) {

		Map<String, Object> result = new HashMap<String, Object>();

		// 로그인 유저 확인
		String token = memberService.login(member);
		Member savedMember = memberService.findMember(member);
		result.put("access-token", token);
		result.put("loginMember", savedMember);

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
}
