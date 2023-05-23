package com.ssafy.ssafit.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Video {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long videoNo;
	
	@Column(nullable = false)
	private String videoId;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String partName;
	
	@Column(nullable = false)
	private String url;
	
	@Column(nullable = false)
	private String channelName;
	
	@ColumnDefault("0")
	private int viewCnt;

	@CreationTimestamp
	private LocalDateTime regDate;
	
	@UpdateTimestamp
	private LocalDateTime modDate;
	
	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
	private List<VideoComment> videoComments = new ArrayList<>();
	
	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
	private List<Bookmark> bookmarks = new ArrayList<>();
}
