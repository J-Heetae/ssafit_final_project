package com.ssafy.ssafit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.dto.VideoDto;
import com.ssafy.ssafit.service.VideoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
@CrossOrigin(origins="*", allowedHeaders = "*")
public class VideoController {
	
	private final VideoService videoService;
	
	/**
	 * 해당 번호와 일치하는 영상 검색
	 * 
	 * @param no 영상 번호
	 */
	@GetMapping("/{no}")
	public ResponseEntity<VideoDto> findVideo(@PathVariable("no") Long no)  {
		return new ResponseEntity<VideoDto>(videoService.findByNo(no), HttpStatus.OK);
	}
	
	/**
	 * 해당 번호와 일치하는 영상 검색
	 * 조회수 증가
	 * 
	 * @param no 영상 번호
	 */
	@GetMapping("/view/{no}")
	public ResponseEntity<VideoDto> findVideoAndViewIncrease(@PathVariable("no") Long no)  {
		return new ResponseEntity<VideoDto>(videoService.findByNoAndView(no), HttpStatus.OK);
	}
	
	/**
	 * 특정컬럼에서 검색어에 해당하는 영상 조회
	 */
	@GetMapping("/search")
    public ResponseEntity<List<VideoDto>> searchVideos(
            @RequestParam String keyword,
            @RequestParam String option) {
        List<VideoDto> videos = videoService.searchVideos(keyword, option);
        return ResponseEntity.ok(videos);
    }
	
	/**
	 * 조건에 맞춰 전체 영상 조회
	 * 
	 * @param partName 운동 부위
	 * @param orderDirection 정렬방향
	 * @param orderCondition 정렬조건
	 * @param pageable
	 */
	@GetMapping
	public ResponseEntity<List<VideoDto>> findAllVideos(
			@RequestParam(required = false) String partName,
			@RequestParam(defaultValue = "DESC") String orderDirection,
			@RequestParam(defaultValue = "regDate") String orderCondition) {
		return new ResponseEntity<List<VideoDto>>(
				videoService.findAll(partName, orderCondition, orderDirection), HttpStatus.OK);
	}
	
	/**
	 * 영상 등록
	 * 
	 * @param video 등록할 영상
	 * @return 등록된 영상
	 */
	@PostMapping
	public ResponseEntity<Video> registVideo(Video video) {
		return new ResponseEntity<Video>(videoService.insert(video), HttpStatus.CREATED);
	}

	/**
	 * 영상 수정
	 * 
	 * @param updateVideo 수정할 영상
	 * @return 수정된 영상
	 */
	@PutMapping
	public ResponseEntity<Video> updateVideo(Video updateVideo) {
		return new ResponseEntity<Video>(videoService.update(updateVideo), HttpStatus.OK);
	}
	
	/**
	 * 영상 삭제
	 * 
	 * @param no 삭제할 영상 번호
	 * @return
	 */
	@DeleteMapping("/{no}")
	public ResponseEntity<Void> deleteVideo(@PathVariable("no") Long no) {
		videoService.delete(no);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
