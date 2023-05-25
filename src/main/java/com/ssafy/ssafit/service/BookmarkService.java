package com.ssafy.ssafit.service;

import java.util.List;

import com.ssafy.ssafit.domain.Bookmark;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.exception.DuplicatedException;

public interface BookmarkService {

	//즐겨찾기 추가
	Bookmark insert(Long videoNo, String memberId) throws DuplicatedException;
	
	//즐겨찾기 취소
	void delete(Long videoNo, String memberId);
	
	boolean findAllByVideoAndMember(String memberId, Long videoNo);
	
	//즐겨찾기 전체 조회
	List<Bookmark> findAll();
	
	//즐겨찾기 번호로 조회
	Bookmark findByNo(Long bookmarkNo);
	
	//맴버 아이디로 즐겨찾기 리스트 조회
	List<Bookmark> findByMemberId(String memberId);
	
	//영상 번호로 즐겨찾기 리스트 조회
	List<Bookmark> findByVideoNo(Long videoNo);

}
