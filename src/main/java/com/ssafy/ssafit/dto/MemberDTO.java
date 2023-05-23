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
@Setter
@Getter
public class MemberDTO {

	private String memberId;
	private String password;
	private String name;
	private int age;
	private String email;
	private LocalDateTime regDate;
	
}
