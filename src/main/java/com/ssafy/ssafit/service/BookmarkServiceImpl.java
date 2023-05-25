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

	private final BookmarkRepository bookmarkRepository;
	private final VideoRepository videoRepository;
	private final MemberRepository memberRepository;

	/**
	 * 즐겨찾기 추가
	 */
	@Override
	public Bookmark insert(Long videoNo, String memberId) throws DuplicatedException {
		Video video = videoRepository.findById(videoNo)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 영상입니다. : " + videoNo));

		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다. : " + memberId));

		// 이전에 추가한 즐겨찾기 영상인지 확인
		List<Bookmark> findBookmarkList = bookmarkRepository.findAllByVideoAndMember(video, member);

		if (findBookmarkList.size() != 0) { // 중복된 경우
			throw new DuplicatedException(
					"이미 즐겨찾기한 영상입니다. memberId : " + member.getMemberId() + ", videoNo : " + video.getVideoNo());
		}

		Bookmark bookmark = new Bookmark(member, video);

		System.out.println("북마크 추가했다잉");
		System.out.println("영상에 추가 전 : " + video.getBookmarks().size());
		member.getBookmarks().add(bookmark);
		video.getBookmarks().add(bookmark);
		System.out.println("영상에 추가 후 : " + video.getBookmarks().size());

		return bookmarkRepository.save(bookmark);
	}

	/**
	 * 즐겨찾기 삭제
	 */
	@Override
	public void delete(Long videoNo, String memberId) {
		Video video = videoRepository.findById(videoNo)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 영상입니다. : " + videoNo));

		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다. : " + memberId));

		List<Bookmark> bookmarks = bookmarkRepository.findAllByVideoAndMember(video, member);
		
		if(bookmarks.size() == 0) {
			return;
		}
		
		Bookmark findBookmark = bookmarks.get(0);
		
		// 맴버와 비디오의 즐겨찾기 리스트에서 해당 즐겨찾기 삭제
		findBookmark.getMember().getBookmarks().remove(findBookmark);
		findBookmark.getVideo().getBookmarks().remove(findBookmark);

		bookmarkRepository.delete(findBookmark);
	}

	/**
	 * 즐겨찾기 전체 조회
	 */
	@Override
	public List<Bookmark> findAll() {
		return bookmarkRepository.findAll();
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
	public List<Bookmark> findByMemberId(String memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다. : " + memberId));
		return bookmarkRepository.findAllByMember(member);
	}

	/**
	 * 영상으로 즐겨찾기 리스트 조회
	 */
	@Override
	public List<Bookmark> findByVideoNo(Long videoNo) {
		Video video = videoRepository.findById(videoNo)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 영상입니다. : " + videoNo));
		return bookmarkRepository.findAllByVideo(video);
	}

	/**
	 * 맴버 아이디와 영상 번호로 해당 영상을 즐겨찾기 했는지 확인
	 * 
	 * @return 즐겨찾기 했으면 true, 아니면 false
	 */
	@Override
	public boolean findAllByVideoAndMember(String memberId, Long videoNo) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다. : " + memberId));
		Video video = videoRepository.findById(videoNo)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 영상입니다. : " + videoNo));
		
		List<Bookmark> bookmarks = bookmarkRepository.findAllByVideoAndMember(video, member);
		
		if(bookmarks.size() == 0)
			return false;
		return true;
	}
}
