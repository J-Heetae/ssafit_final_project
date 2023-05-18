package com.ssafy.ssafit.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ssafy.ssafit.dto.JoinCntByMonthDto;

public interface AdminService {
	
	@Query()
	List<JoinCntByMonthDto> countMembersByMonth(int year, int month);
	
}