package com.ssafy.ssafit.dto;

import java.util.List;

import com.ssafy.ssafit.domain.BoardComment;
import com.ssafy.ssafit.domain.BoardType;
import com.ssafy.ssafit.domain.File;

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
public class BoardDTO {

	private Long boardNo;
	private String memberId;
	private String title;
	private String content;
	private int viewCnt;
	private BoardType type;
	private String gym;
	private String regDate;
	private String modDate;

	private int commentCnt;
	private Long likesCnt;

	private List<BoardComment> comments;
	private File file;

	@Override
	public String toString() {
		return "BoardDTO [boardNo=" + boardNo + ", memberId=" + memberId + ", title=" + title + ", content=" + content
				+ ", viewCnt=" + viewCnt + ", type=" + type + ", gym=" + gym + ", regDate=" + regDate + ", modDate="
				+ modDate + "]";
	}

}
