package com.ssafy.ssafit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class VideoCommentDto {
	Long commentNo;
	Long videoNo;
	String memberId;
	String content;
	String regDate;
	String modDate;

}
