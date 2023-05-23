package com.ssafy.ssafit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.dto.VideoDto;
import com.ssafy.ssafit.exception.DuplicatedException;
import com.ssafy.ssafit.exception.NotFoundException;


public interface VideoService {
	
	//영상 등록
	Video insert(Video video) throws DuplicatedException;
	
	//영상 삭제
	void delete(Long no);
	
	//영상 수정
	Video update(Video video) throws DuplicatedException;
	
	//영상 번호로 영상 조회
	VideoDto findByNo(Long no) throws NotFoundException;
	
	//검색어랑 검색 기준으로 영상 조회
	List<VideoDto> searchVideos(String keyword, String searchCriteria);
	
	//모든 영상 조회
	List<VideoDto> findAll(String partName, String orderCondition, String orderDirection);
}
