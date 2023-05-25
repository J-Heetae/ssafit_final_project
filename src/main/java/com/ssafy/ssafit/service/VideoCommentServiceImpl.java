package com.ssafy.ssafit.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.domain.VideoComment;
import com.ssafy.ssafit.domain.asset.OrderDirection;
import com.ssafy.ssafit.dto.VideoCommentDto;
import com.ssafy.ssafit.dto.VideoDto;
import com.ssafy.ssafit.exception.NotFoundException;
import com.ssafy.ssafit.repository.VideoCommentRepository;
import com.ssafy.ssafit.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VideoCommentServiceImpl implements VideoCommentService {

	private final VideoCommentRepository commentRepository;
	private final VideoRepository videoRepository;

	@Override
	public VideoCommentDto insert(VideoComment comment) {
		VideoComment c = commentRepository.save(comment);
		
		return new VideoCommentDto(c.getCommentNo(), c.getVideo().getVideoNo(), c.getMember().getMemberId(),
				c.getContent(), c.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
				c.getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	@Override
	public VideoComment update(VideoComment comment) {
		VideoComment savedComment = commentRepository.findById(comment.getCommentNo()).orElse(null);

		if (savedComment == null)
			throw new NotFoundException("등록된 댓글을 찾을 수 없습니다.");

		// 제목 및 내용 변경
//		savedComment.setTitle(comment.getTitle());
		savedComment.setContent(comment.getContent());

		// 변경된 엔티티 리턴
		return savedComment;
	}

	@Override
	public void delete(Long commentId) {
		commentRepository.deleteById(commentId);
	}

	@Override
	public VideoCommentDto findByNo(Long commentNo) {
		VideoComment c = commentRepository.findById(commentNo)
				.orElseThrow(() -> new NotFoundException("등록된 댓글을 찾을 수 없습니다. : " + commentNo));

		return new VideoCommentDto(c.getCommentNo(), c.getVideo().getVideoNo(), c.getMember().getMemberId(),
				c.getContent(), c.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
				c.getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	@Override
	public List<VideoCommentDto> findAllComments(Long videoNo) {
		Video video = videoRepository.findById(videoNo).orElse(null);
		
		if(video != null) {
			List<VideoComment> comments = commentRepository.findByVideo(video);
			
			List<VideoComment> newest = new ArrayList<>();
			for(int i=comments.size()-1; i>=0; i--) {
				newest.add(comments.get(i));
			}
			
			return videoCommentToDto(newest);
		}
		return null;
	}
	
	private List<VideoCommentDto> videoCommentToDto(List<VideoComment> comments) {
		return comments.stream()
				.map(c -> new VideoCommentDto(c.getCommentNo(), c.getVideo().getVideoNo(), c.getMember().getMemberId(),
						c.getContent(), c.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
						c.getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.collect(Collectors.toList());
	}
}
