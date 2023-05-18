package com.ssafy.ssafit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssafy.ssafit.domain.Video;
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
	Video findByNo(Long no) throws NotFoundException;
	
	//모든 영상 조회
	Page<Video> findAll(Pageable pageable, String partName, String orderCondition, String orderDirection);
	
	//은동 부위로 영상 조회
	List<Video> findByPartName(String partName);
	
	//정렬 기준으로 영상 조회
	List<Video> findOrderByViewCnt(String orderDir);
}
