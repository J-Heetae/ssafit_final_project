package com.ssafy.ssafit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.ssafit.domain.Bookmark;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.Video;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{
	
	List<Bookmark> findAllByVideo(Video video);
	
	List<Bookmark> findAllByMember(Member member);
	
	List<Bookmark> findAllByVideoAndMember(Video video, Member member);
}
