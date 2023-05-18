package com.ssafy.ssafit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ssafy.ssafit.domain.Bookmark;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.exception.DuplicatedException;
import com.ssafy.ssafit.exception.NotFoundException;
import com.ssafy.ssafit.repository.BookmarkRepository;
import com.ssafy.ssafit.repository.MemberRepository;
import com.ssafy.ssafit.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

	private final VideoRepository videoRepository;
	private final MemberRepository memberRepository;
	private final BookmarkRepository bookmarkRepository;

	/**
	 * 즐겨찾기 추가
	 */
	@Override 
	public void insert(Video video, Member member) throws DuplicatedException { 
		Member findMember = memberRepository.findById(member.getMemberId())
				.orElseThrow(() -> new NotFoundException("해당 아이디의 멤버가 존재하지 않습니다. : " + member.getMemberId()));
		 
		Video findVideo = videoRepository.findById(video.getVideoNo())
				.orElseThrow(() -> new NotFoundException("해당 번호의 영상이 존재하지 않습니다. : " + video.getVideoNo()));
		  
		//이전에 추가한 즐겨찾기 영상인지 확인 
		List<Bookmark> findBookmarkList = bookmarkRepository.findAllByVideoAndMember(video, member);
		 
		if(findBookmarkList != null) { //중복된 경우
			throw new DuplicatedException("이미 즐겨찾기한 영상입니다. memberId : " + member.getMemberId() + ", videoNo : " + video.getVideoNo());
		}
	 
	 	Bookmark bookmark = new Bookmark(findMember, findVideo);
	 
	 	findMember.getBookmarks().add(bookmark);
	 	findVideo.getBookmarks().add(bookmark); //저장
	 	bookmarkRepository.save(bookmark); 
	 }
	

	/**
	 * 즐겨찾기 삭제
	 */
	@Override
	public void delete(Long bookmarkNo) {
		Bookmark findBookmark = findByNo(bookmarkNo);
		bookmarkRepository.delete(findBookmark);
	}

	/**
	 * 번호로 즐겨찾기 조회
	 */
	@Override
	public Bookmark findByNo(Long bookmarkNo) {
		return bookmarkRepository.findById(bookmarkNo)
				.orElseThrow(() -> new NotFoundException("해당 번호의 즐겨찾기는 존재하지 않습니다. : " + bookmarkNo));
	}

	/**
	 * 맴버로 즐겨찾기 리스트 조회
	 */
	@Override
	public List<Bookmark> findByMember(Member member) {
		return bookmarkRepository.findAllByMember(member);
	}

	/**
	 * 영상으로 즐겨찾기 리스트 조회
	 */
	@Override
	public List<Bookmark> findByVideo(Video video) {
		return bookmarkRepository.findAllByVideo(video);
	}
}
