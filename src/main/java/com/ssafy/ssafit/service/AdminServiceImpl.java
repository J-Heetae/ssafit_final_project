package com.ssafy.ssafit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ssafy.ssafit.dto.JoinCntByMonthDto;
import com.ssafy.ssafit.repository.MemberRepository;
import com.ssafy.ssafit.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
	
	private final MemberRepository memberRepository;
	private final VideoRepository videoRepository;
	

	@Override
	public List<JoinCntByMonthDto> countMembersByMonth(int year, int month) {
		return memberRepository.countMembersByMonth(year);
	}
}
