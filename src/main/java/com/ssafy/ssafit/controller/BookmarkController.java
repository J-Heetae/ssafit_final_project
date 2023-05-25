package com.ssafy.ssafit.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.ssafit.domain.Bookmark;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.dto.VideoDto;
import com.ssafy.ssafit.repository.BookmarkRepository;
import com.ssafy.ssafit.service.BookmarkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BookmarkController {

	private final BookmarkService bookmarkService;

	/**
	 * 회원 아이디로 북마크한 영상 조회
	 * 
	 * @param memberId
	 * @return 북마크한 영상 리스트
	 */
	@GetMapping("/member/{id}")
	public ResponseEntity<List<VideoDto>> findBookmarkedVideos(@PathVariable("id") String memberId) {
		List<Bookmark> bookmarks = bookmarkService.findByMemberId(memberId);

		List<Video> videos = new ArrayList<>();
		for (Bookmark bookmark : bookmarks) {
			videos.add(bookmark.getVideo());
		}

		List<VideoDto> result = videos.stream()
				.map(v -> new VideoDto(v.getVideoNo(), v.getVideoId(), v.getTitle(), v.getPartName(), v.getUrl(),
						v.getChannelName(), v.getViewCnt(), v.getBookmarks().size(), v.getVideoComments().size(),
						v.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
						v.getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.collect(Collectors.toList());

		return new ResponseEntity<List<VideoDto>>(result, HttpStatus.OK);
	}

	/**
	 * 영상 번호로 북마크한 회원 조회
	 * 
	 * @param videoNo
	 * @return 북마크한 회원 리스트
	 */
	@GetMapping("/vidoe/{no}")
	public ResponseEntity<List<Member>> findBookmarkedMembers(@PathVariable("no") Long videoNo) {
		List<Bookmark> bookmarks = bookmarkService.findByVideoNo(videoNo);

		List<Member> members = new ArrayList<>();

		for (Bookmark bookmark : bookmarks) {
			members.add(bookmark.getMember());
		}
		return new ResponseEntity<List<Member>>(members, HttpStatus.OK);
	}

	/**
	 * 맴버 아이디와 영상 번호로 해당 영상을 즐겨찾기 했는지 확인
	 * 
	 * @return 즐겨찾기 했으면 true, 아니면 false
	 */
	@GetMapping("/{memberId}/{videoNo}")
	public ResponseEntity<Boolean> isBookmarked(@PathVariable("memberId") String memberId,
			@PathVariable("videoNo") Long videoNo) {
		return new ResponseEntity<Boolean>(bookmarkService.findAllByVideoAndMember(memberId, videoNo), HttpStatus.OK);
	}

	/**
	 * 즐겨찾기 등록
	 * 
	 * @param memberId 즐겨찾기를 한 멤버
	 * @param videoNo  즐겨찾기 된 영상
	 */
	@PostMapping("/{memberId}/{videoNo}")
	public ResponseEntity<Void> addBookmark(@PathVariable("memberId") String memberId,
			@PathVariable("videoNo") Long videoNo) {
		bookmarkService.insert(videoNo, memberId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * 즐겨찾기 취소
	 * 
	 * @param no 즐겨찾기 번호
	 */
	@DeleteMapping("/{memberId}/{videoNo}")
	public ResponseEntity<Void> deleteBookmark(@PathVariable("memberId") String memberId,
			@PathVariable("videoNo") Long videoNo) {
		bookmarkService.delete(videoNo, memberId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
