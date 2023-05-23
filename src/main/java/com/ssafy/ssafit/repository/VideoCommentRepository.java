package com.ssafy.ssafit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.domain.VideoComment;

public interface VideoCommentRepository extends JpaRepository<VideoComment, Long> {

	List<VideoComment> findByVideo(Video video);
}
