package com.ssafy.ssafit.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.domain.VideoComment;
import com.ssafy.ssafit.dto.VideoCommentDto;

@SpringBootTest
@Transactional
public class VideoCommentServiceTest {
	
	@Autowired VideoCommentService videoCommentService;
	@Autowired MemberService memberService;
	@Autowired VideoService videoService;
	
	private static Member member;
	private static Video video;
	
	@BeforeEach
	public void before() {
		Video beforeVideo = new Video();
		beforeVideo.setVideoId("video100");
		beforeVideo.setTitle("첫 번째 동영상");
		beforeVideo.setPartName("파트 1");
		beforeVideo.setUrl("https://example.com/vide12312323");
		beforeVideo.setChannelName("채널 1");
		
		video = videoService.insert(beforeVideo);
		
		Member beforeMember = new Member();
		beforeMember.setMemberId("member1");
		beforeMember.setPassword("password1");
		beforeMember.setName("John");
		beforeMember.setAge(25);
		beforeMember.setEmail("john@example.com");
        
		member = memberService.join(beforeMember);
	}
	
	@Test
	public void 영상댓글등록() {
        VideoComment comment = new VideoComment();
        comment.setMember(member);
        comment.setTitle("Dummy Comment");
        comment.setContent("This is a dummy comment.");
        comment.setVideo(video);
        
        VideoCommentDto savedComment = videoCommentService.insert(comment);
	
        assertThat(savedComment).isEqualTo(comment);
	}
	
//	@Test
	public void 영상댓글수정() {
		VideoComment comment = new VideoComment();
        comment.setMember(member);
        comment.setTitle("Dummy Comment");
        comment.setContent("This is a dummy comment.");
        comment.setVideo(video);
	        
        VideoCommentDto savedComment = videoCommentService.insert(comment);
        
        savedComment.setContent("content");
	}
	
	@Test
	public void 영상댓글삭제() {
		
	}
	
	@Test
	public void 영상댓글전체조회() {
		
	}
}
