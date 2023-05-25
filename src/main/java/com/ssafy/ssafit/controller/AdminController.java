package com.ssafy.ssafit.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.ssafit.dto.AdminMemberDTO;
import com.ssafy.ssafit.dto.JoinCntByMonthDto;
import com.ssafy.ssafit.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

	private final MemberRepository memberRepository;

	@GetMapping("/join")
	public ResponseEntity<List<JoinCntByMonthDto>> getCountMembersByMonth() {
		return new ResponseEntity<List<JoinCntByMonthDto>>(memberRepository.countMembersByMonth(2023), HttpStatus.OK);
	}

	@GetMapping("/member")
	public ResponseEntity<List<AdminMemberDTO>> getMembers() {
		List<AdminMemberDTO> members = memberRepository.findAll().stream().map(m -> new AdminMemberDTO(m.getMemberId(),
				m.getPassword(), m.getName(), m.getAge(), m.getEmail(), m.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).collect(Collectors.toList());

		return new ResponseEntity<List<AdminMemberDTO>>(members, HttpStatus.OK);
	}
}
