package com.ssafy.ssafit.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BoardCommentDTO {
	private Long commentNo;
	private String memberId;
	private Long boardNo;
	private String content;
	private LocalDateTime regDate;
}
