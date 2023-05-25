package com.ssafy.ssafit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LikesDTO {
	
	private Long likesNo;
	private Long boardNo;
	private String memberId;
}
