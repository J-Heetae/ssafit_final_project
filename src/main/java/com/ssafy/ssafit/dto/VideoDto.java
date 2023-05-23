package com.ssafy.ssafit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class VideoDto {
	Long videoNo;
	String videoId;
	String title;
	String partName;
	String url;
	String channelName;
	int viewCnt;
	int bookmarkCnt;
	int commentCnt;
	String regDate;
	String modDate;
}
