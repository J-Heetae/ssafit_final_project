package com.ssafy.ssafit.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	private List<Likes> likes = new ArrayList<>();

	@OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
	private File file;

	@JsonIgnore
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	private List<BoardComment> comments = new ArrayList<>();

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	@Lob
	private String content;

	// 디폴트값 0으로 설정
	@ColumnDefault("0")
	private int viewCnt;

	@Enumerated(EnumType.STRING)
	private BoardType type;

	@Column(nullable = true)
	private String gym;

	@CreationTimestamp
	private LocalDateTime regDate;

	@UpdateTimestamp
	private LocalDateTime modDate;
}
