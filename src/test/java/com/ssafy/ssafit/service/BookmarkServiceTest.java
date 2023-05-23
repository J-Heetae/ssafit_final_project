package com.ssafy.ssafit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.ssafit.domain.Bookmark;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.dto.VideoDto;

@SpringBootTest
@Transactional
public class BookmarkServiceTest {
	
	@Autowired
	BookmarkService bookmarkService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	VideoService videoService;
	
	@BeforeEach
	public void before() {
		//맴버 생성
		Member member = new Member();
        member.setMemberId("member1");
        member.setPassword("password1");
        member.setName("John");
        member.setAge(25);
        member.setEmail("john@example.com");
        
        memberService.join(member);
        
        //영상 생성
        List<Video> videos = new ArrayList<>();
        
        for(int i=1; i<=3; i++) {
	        Video video = new Video();
	        video.setVideoId("video" + i);
	        video.setTitle("Video" + i);
	        video.setPartName("Part" + i);
	        video.setUrl("https://example.com/video" + i);
	        video.setChannelName("Channel" + i);
	        
	        videos.add(video);
	        videoService.insert(video);
        }
        
        for(Video video : videos) {
        	bookmarkService.insert(video.getVideoNo(), member.getMemberId());
        }
	}
	
	@Test
	public void 즐겨찾기등록() {
		//맴버 생성
		Member member1 = new Member();
	    member1.setMemberId("member");
	    member1.setPassword("password");
	    member1.setName("John");
	    member1.setAge(25);
	    member1.setEmail("john1312@example.com");
	    
	    memberService.join(member1);
	    
	    System.out.println("맴버 등록 성공");
	    
	    //비디오 생성
	    Video video1 = new Video();
	    video1.setVideoId("video");
	    video1.setTitle("Video");
	    video1.setPartName("Part");
	    video1.setUrl("https://example.com/vide");
	    video1.setChannelName("Channe");
	    
	    videoService.insert(video1);
	    
	    System.out.println("영상 등록 성공");
	    
	    //즐겨찾기 등록
		bookmarkService.insert(video1.getVideoNo(), member1.getMemberId());
		
		assertThat(bookmarkService.findAll().size()).isEqualTo(4);
	}
	
	@Test
	public void 즐겨찾기전체조회() {
		assertThat(bookmarkService.findAll().size()).isEqualTo(3);
	}
	
	@Test
	public void 즐겨찾기번호로조회() {
		//맴버 생성
		Member member1 = new Member();
	    member1.setMemberId("member");
	    member1.setPassword("password");
	    member1.setName("John");
	    member1.setAge(25);
	    member1.setEmail("john1312@example.com");
	    
	    memberService.join(member1);
	    
	    System.out.println("맴버 등록 성공");
	    
	    //비디오 생성
	    Video video1 = new Video();
	    video1.setVideoId("video");
	    video1.setTitle("Video");
	    video1.setPartName("Part");
	    video1.setUrl("https://example.com/vide");
	    video1.setChannelName("Channe");
	    
	    videoService.insert(video1);
	    
	    System.out.println("영상 등록 성공");
	    
	    //즐겨찾기 등록
		Bookmark savedBookmark = bookmarkService.insert(video1.getVideoNo(), member1.getMemberId());
		Long savedBookmarkNo = savedBookmark.getBookmarkNo();
		
		Bookmark findBookmark = bookmarkService.findByNo(savedBookmarkNo);
		
		assertThat(savedBookmark).isEqualTo(findBookmark);
	}
	
	@Test
	public void 맴버로조회() {
		//맴버 생성
		Member member1 = new Member();
	    member1.setMemberId("member");
	    member1.setPassword("password");
	    member1.setName("John");
	    member1.setAge(25);
	    member1.setEmail("john1312@example.com");
	    
	    Member savedMember = memberService.join(member1);
	    
	    //비디오 생성
	    Video video1 = new Video();
	    video1.setVideoId("video");
	    video1.setTitle("Video");
	    video1.setPartName("Part");
	    video1.setUrl("https://example.com/vide");
	    video1.setChannelName("Channe");
	    
	    videoService.insert(video1);
	    	    
	    //즐겨찾기 등록
		bookmarkService.insert(video1.getVideoNo(), member1.getMemberId());
		
		assertThat(bookmarkService.findByMemberId(savedMember.getMemberId()).size()).isEqualTo(1);
	}
	
	@Test
	public void 비디오로조회() {
		String partName = null;
		String orderCondition = "videoNo";
		String orderDirection = "ASC";
		
		List<VideoDto> videos = videoService.findAll(partName, orderCondition, orderDirection);
		
		for(VideoDto video : videos) {
			assertThat(bookmarkService.findByVideoNo(video.getVideoNo()).size()).isEqualTo(1);
		}
	}
}
