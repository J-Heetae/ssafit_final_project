package com.ssafy.ssafit.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafit.domain.QVideo;
import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.domain.asset.OrderDirection;
import com.ssafy.ssafit.exception.DuplicatedException;
import com.ssafy.ssafit.exception.NotFoundException;
import com.ssafy.ssafit.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService{
	
	private final VideoRepository videoRepository;
	private final JPAQueryFactory queryFactory;
	
	private QVideo video = QVideo.video;

	/**
	 * 영상 등록
	 */
	@Override
	public Video insert(Video video) throws DuplicatedException {
		validateDuplicateVideo(video); //중복 확인
		return videoRepository.save(video);
	}
	
	/**
	 * 영상 중복 확인
	 */
	private void validateDuplicateVideo(Video video) throws DuplicatedException {
		if(videoRepository.existsByTitle(video.getTitle())) {
			throw new DuplicatedException("이미 존재하는 영상 제목입니다. : " + video.getTitle());
		}
		if(videoRepository.existsByUrl(video.getUrl())) {
			throw new DuplicatedException("이미 존재하는 영상 url입니다. : " + video.getUrl());
		}
		if(videoRepository.existsByVideoId(video.getVideoId())) {
			throw new DuplicatedException("이미 존재하는 영상 id입니다. : " + video.getVideoId());
		}
	}

	/**
	 * 영상 삭제
	 */
	@Override
	public void delete(Long no) {
		Video findVideo = findByNo(no);
		videoRepository.delete(findVideo);
	}

	/**
	 * 영상 수정
	 */
	@Override
	public Video update(Video video) throws DuplicatedException {
		validateDuplicateVideo(video); //중복 확인
		
		Video findVideo = findByNo(video.getVideoNo());
		
		//영상 정보 수정
		findVideo.setChannelName(video.getChannelName());
		findVideo.setTitle(video.getTitle());
		findVideo.setPartName(video.getPartName());
		findVideo.setUrl(video.getUrl());
		findVideo.setVideoId(video.getVideoId());
		
		return videoRepository.save(findVideo);
	}
	
	/**
	 * 영상 번호로 조회
	 */
	@Override
	public Video findByNo(Long no) throws NotFoundException {
		return videoRepository.findById(no)
				.orElseThrow(() -> new NotFoundException("해당 번호의 영상이 존재하지 않습니다. : " + no));
	}

	/**
	 * 해당 운동 부위의 모든 영상 조회
	 */
	@Override
	public List<Video> findByPartName(String partName) {
		return videoRepository.findByPartName(partName);
	}

	/**
	 * 해당 정렬 기준으로 영상 조회
	 */
	@Override
	public List<Video> findOrderByViewCnt(String orderDirection) {
		if(orderDirection.toUpperCase().equals(OrderDirection.ASC)) {
			return videoRepository.findAllByOrderByViewCntAsc();
		}
		return videoRepository.findAllByOrderByViewCntDesc();
	}
	
	@Override
	public Page<Video> findAll(Pageable pageable, String partName, String orderCondition, String orderDirection) {
		// 정렬 조건 생성
	    Sort sort = sortByOrderCondition(orderCondition, orderDirection);
	    // 게시글 조회 쿼리
	    List<Video> videos = queryFactory.selectFrom(video)
	            // 검색 조건: 게시판 타입이 주어진 경우에만 적용
	            .where(eqPartName(partName))
	            // 정렬 조건: 생성된 정렬 조건을 적용
	            .orderBy(getOrderSpecifier(sort).stream().toArray(OrderSpecifier[]::new))
	            // 페이징 처리: 페이지 정보에 따라 offset과 limit 설정
	            .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	    // 전체 게시글 수 조회 쿼리
	    JPAQuery<Long> countQuery = queryFactory.select(video.count()).from(video).where(eqPartName(partName));
	    // 페이징 처리된 게시글 목록 반환
	    return PageableExecutionUtils.getPage(videos, pageable, countQuery::fetchOne);
	}

	// 게시판별 검색, 조건이 없으면 null 반환
	private BooleanExpression eqPartName(String partName) {
	    // 게시판 타입이 주어진 경우에만 조건 생성
	    return partName != null ? video.partName.eq(partName) : null;
	}

	// 정렬 조건 생성
	private Sort sortByOrderCondition(String orderCondition, String orderDirection) {
	    // 기본 정렬 조건: 등록일순, 내림차순
	    Sort sort = Sort.by(Direction.DESC, "regDate");
	    Direction direction = null;

	    if (orderCondition != null) { // 검색 조건이 null이 아닌 경우
	        if (orderDirection.toUpperCase().equals(OrderDirection.ASC)) { //오름차순 정렬
	            direction = Direction.ASC;
	        } else { // 기본 정렬 조건: DESC
	            direction = Direction.DESC;
	        }
	        // 지정된 정렬 조건으로 Sort 객체 생성
	        sort = Sort.by(direction, orderCondition);
	    }
	    return sort;
	}

	// OrderSpecifier 목록 생성
	private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
	    List<OrderSpecifier> orderBy = new ArrayList<OrderSpecifier>();

	    // 각 정렬 조건에 대해 OrderSpecifier 생성
	    sort.stream().forEach(order -> {
	        // 정렬 방향
	        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
	        
	        // 정렬 대상 속성
	        String prop = order.getProperty();

	        // OrderSpecifier 생성을 위한 PathBuilder 설정
	        PathBuilder<Video> pathBuilder = new PathBuilder<Video>(Video.class, "video");

	        // OrderSpecifier 생성 및 목록에 추가
	        orderBy.add(new OrderSpecifier(direction, pathBuilder.get(prop)));
	    });
	    return orderBy;
	}
}
