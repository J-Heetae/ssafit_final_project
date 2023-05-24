package com.ssafy.ssafit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.dto.VideoDto;
import com.ssafy.ssafit.exception.DuplicatedException;

@SpringBootTest
@Transactional
public class VideoServiceTest {
	
	@Autowired
	VideoService videoService;
	
	@BeforeEach
	public void before() {
		for(int i=0; i<10; i++) {
			Video video = new Video();
	        video.setVideoId("video" + i);
	        video.setTitle( (i+1) + "번째 동영상");
	        video.setPartName("파트 " + i);
	        video.setUrl("https://example.com/video" + i);
	        video.setChannelName("채널" + i);
	        
	        videoService.insert(video);
		}
	}
	
	@Test
	public void 영상등록테스트() {
		Video video = new Video();
        video.setVideoId("video100");
        video.setTitle("첫 번째 동영상");
        video.setPartName("파트 1");
        video.setUrl("https://example.com/vide12312323");
        video.setChannelName("채널 1");
        
        Video savedVideo = videoService.insert(video);
        VideoDto findVideo = videoService.findByNo(savedVideo.getVideoNo());

        assertThat(findVideo).isEqualTo(video);
	}
	
	@Test
	public void 중복테스트() {
        Video video1 = new Video();
        video1.setVideoId("video1");
        video1.setTitle("1번째 동영상");
        video1.setPartName("파트 1");
        video1.setUrl("https://example.com/video1");
        video1.setChannelName("채널1");
        
        assertThrows(DuplicatedException.class, () -> {
        	videoService.insert(video1);
        });
	}
	
	@Test
	public void 영상삭제() {
		String partName = null;
		String orderCondition = "title";
		String orderDirection = "ASC";
		
		List<VideoDto> list1 = videoService.findAll(partName, orderCondition, orderDirection);

		System.out.println("list 1 ------------------");
		for(VideoDto video : list1) {
			System.out.println(video.toString());
		}
		
		videoService.delete(list1.get(0).getVideoNo());
		
		List<VideoDto> list2 = videoService.findAll(partName, orderCondition, orderDirection);
		
		System.out.println("list 2 ------------------");
		for(VideoDto video : list2) {
			System.out.println(video.toString());
		}
		
		assertEquals(9,list2.size());
	}
	
	@Test
	public void 영상수정() {
		String partName = null;
		String orderCondition = "title";
		String orderDirection = "ASC";
		List<VideoDto> list = videoService.findAll(partName, orderCondition, orderDirection);
		
		
		VideoDto video = list.get(0);
		Long videoNo = video.getVideoNo();
		
		video.setChannelName("하이");
		video.setPartName("바이");
		video.setUrl("굿바이");
		
//		videoService.update(video);
		
		VideoDto updatedVideo = videoService.findByNo(videoNo);
		
		assertThat(updatedVideo.getChannelName()).isEqualTo("하이");
		assertThat(updatedVideo.getPartName()).isEqualTo("바이");
		assertThat(updatedVideo.getUrl()).isEqualTo("굿바이");
	}
	
	@Test
	public void 운동부위로검색() {
		String partName = "파트 1";
		String orderCondition = "title";
		String orderDirection = "ASC";
		
		List<VideoDto> list = videoService.findAll(partName, orderCondition, orderDirection);
		
		assertThat(list.size()).isEqualTo(1);
		assertThat(list.get(0).getPartName()).isEqualTo("파트 1");
	}
	
	@Test
	public void 검색조건으로검색() {
		String keyword = "3";
		String searchCriteria = "";
		
		List<VideoDto> list = videoService.searchVideos(keyword, searchCriteria);
		
		for(VideoDto video : list) {
			System.out.println("전체 검색으로 찾은 비디오 " + video.toString());
		}
		
		keyword = "3";
		searchCriteria = "title";
		
		list = videoService.searchVideos(keyword, searchCriteria);
		
		for(VideoDto video : list) {
			System.out.println("제목 검색으로 찾은 비디오 " + video.toString());
		}
		
		keyword = "3";
		searchCriteria = "channelName";
		
		list = videoService.searchVideos(keyword, searchCriteria);
		
		for(VideoDto video : list) {
			System.out.println("채널명 검색으로 찾은 비디오 " + video.toString());
		}
	}
	
	@Test
	public void 정렬기준으로검색() {
		String partName = null;
		String orderCondition = "videoNo";
		String orderDirection = "ASC";
		
		System.out.println("find list1");
		List<VideoDto> list1 = videoService.findAll(partName, orderCondition, orderDirection);
		
		System.out.println("list1-------------------------");
		for(VideoDto video : list1) {
			System.out.println(video.toString());
		}
		
		orderDirection = "DESC";
		
		System.out.println("find list2");
		List<VideoDto> list2 = videoService.findAll(partName, orderCondition, orderDirection);
		
		System.out.println("list2-------------------------");
		for(VideoDto video : list2) {
			System.out.println(video.toString());
		}
	}
}
