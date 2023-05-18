package com.ssafy.ssafit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.dto.JoinCntByMonthDto;
import com.ssafy.ssafit.dto.PercentageOfPartDto;


@SpringBootTest
@Transactional
public class VideoRepositoryTest {
	
	@Autowired
	private VideoRepository videoRepository;

	@Test
	public void 운동부위별퍼센트() throws Exception {
		//Given
		for (int i = 1; i <= 20; i++) {
            Video video = new Video();
            video.setVideoId("video" + i);
            video.setTitle("Video " + i);
            video.setPartName("Part " + i);
            video.setUrl("https://example.com/video" + i);
            video.setChannelName("Channel " + i);
            video.setViewCnt(i * 100); // 임의의 조회수 설정

            videoRepository.save(video);
        }
		
		//When
        List<PercentageOfPartDto> findPercents = videoRepository.getPercentEachPart();
		
		//Then
        assertThat(findPercents.size()).isEqualTo(20);
        assertThat(findPercents.get(0).getPercentage() == 5);
	}
}
