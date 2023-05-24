package com.ssafy.ssafit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.dto.PercentageOfPartDto;

public interface VideoRepository extends JpaRepository<Video, Long>, QuerydslPredicateExecutor<Video>{
	
	//해당 제목의 영상이 있는지 조회
	boolean existsByTitle(String title);
	
	//해당 url의 영상이 있는지 조회
	boolean existsByUrl(String url);
	
	//해당 id의 영상이 있는지 조회
	boolean existsByVideoId(String videoId);
	
	//운동 부위로 조회
	List<Video> findByPartName(String partName);
	
	//조회수 기준 오름차순 정렬
	List<Video> findAllByOrderByViewCntAsc();
	
	//조회수 기준 내림차순 정렬
	List<Video> findAllByOrderByViewCntDesc();

	//운동 부위 별로 % 조회
	@Query("SELECT v.partName as partName, ROUND(COUNT(v) / (SELECT COUNT(v) FROM Video v) * 100) as percentage FROM Video v GROUP BY v.partName")
	List<PercentageOfPartDto> getPercentEachPart();
}
