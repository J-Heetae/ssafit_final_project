package com.ssafy.ssafit.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardComment extends Comment {
	
	
	public BoardComment(Long commentNo, Member member, String content, LocalDateTime regDate, LocalDateTime modDate, Board board) {
		super(commentNo, member, content, regDate, modDate);
		this.board = board;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_no")
	private Board board;
}
