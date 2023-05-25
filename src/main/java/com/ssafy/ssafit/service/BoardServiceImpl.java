package com.ssafy.ssafit.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.BoardType;
import com.ssafy.ssafit.domain.File;
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.QBoard;
import com.ssafy.ssafit.domain.asset.OrderDirection;
import com.ssafy.ssafit.dto.BoardDTO;
import com.ssafy.ssafit.exception.NotFoundException;
import com.ssafy.ssafit.repository.BoardCommentRepository;
import com.ssafy.ssafit.repository.BoardRepository;
import com.ssafy.ssafit.repository.FileRepository;
import com.ssafy.ssafit.repository.LikesRepository;
import com.ssafy.ssafit.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final BoardCommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final LikesRepository likesRespository;
	private final FileRepository fileRepository;

	private final JPAQueryFactory queryFactory;

	private QBoard board = QBoard.board;

	@Override
	public BoardDTO insert(BoardDTO board, File file) {

		// 게시글 엔티티 생성
		Board entity = Board.builder().title(board.getTitle()).content(board.getContent()).type(board.getType())
				.gym(board.getGym()).build();

		// 회원 찾아서 게시글에 저장
		entity.setMember(memberRepository.findById(board.getMemberId()).orElseThrow(() -> {
			throw new NotFoundException("회원 정보가 일치하지 않습니다.");
		}));

		// 게시글 저장
		Board savedBoard = boardRepository.save(entity);

		if (file != null) {
			// 파일 엔티티에 게시글 저장
			file.setBoard(savedBoard);

			// 파일 엔티티 추가
			File savedFile = insertFile(file);

			// 게시글 엔티티에 추가
			savedBoard.setFile(savedFile);
		}

		return getBoardDTO(savedBoard);
	}

	private File insertFile(File file) {
		return fileRepository.save(file);
	}

	@Override
	public BoardDTO update(BoardDTO board) {
		Board savedBoard = boardRepository.findById(board.getBoardNo()).orElse(null);

		// 게시글 번호에 해당하는 게시글이 없으면 예외 발생
		if (savedBoard == null)
			throw new NotFoundException("해당하는 게시물을 찾을 수 없습니다.");

		// 제목, 내용 수정
		savedBoard.setTitle(board.getTitle());
		savedBoard.setContent(board.getContent());

		return getBoardDTO(savedBoard);
	}

	@Override
	public BoardDTO getBoardDTO(Board savedBoard) {
		return BoardDTO.builder().boardNo(savedBoard.getBoardNo()).memberId(savedBoard.getMember().getMemberId())
				.title(savedBoard.getTitle()).content(savedBoard.getContent()).viewCnt(savedBoard.getViewCnt())
				.type(savedBoard.getType())
				.regDate(savedBoard.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.modDate(savedBoard.getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.gym(savedBoard.getGym()).comments(commentRepository.findByBoard(savedBoard))
				.commentCnt(commentRepository.countByBoard(savedBoard))
				.likesCnt(likesRespository.countByBoard(savedBoard))
//				.comments(savedBoard.getComments())
				.file(savedBoard.getFile()).build();
	}

	@Override
	public void delete(Long boardId) {
		// 기존에 해당 게시글이 있는지 null 체크
		// null이면 예외 발생
		this.findByBoardId(boardId);

		// 글 삭제
		boardRepository.deleteById(boardId);
	}

	// 게시글 전체 조회 - 게시판별, 등록일 순, 조회수 순
	@Override
	public Page<Board> findAll(Pageable pageable, BoardType boardType, String orderCondition, String orderDirection) {
		Sort sort = sortByOrderCondition(orderCondition, orderDirection);

		List<Board> boards = queryFactory.selectFrom(board)
				// 검색 조건
				.where(eqBoardType(boardType))
				// 정렬 조건
				.orderBy(getOrderSpecifier(sort).stream().toArray(OrderSpecifier[]::new))
				// 페이징 처리
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
		// 페이징 처리
		JPAQuery<Long> countQuery = queryFactory.select(board.count()).from(board).where(eqBoardType(boardType));

		return PageableExecutionUtils.getPage(boards, pageable, countQuery::fetchOne);
	}

	// 게시글 전체 조회 - 게시판별, 등록일 순, 조회수 순
	@Override
	public List<BoardDTO> findAllBoard(BoardType boardType, String orderCondition, String orderDirection) {
		Sort sort = sortByOrderCondition(orderCondition, orderDirection);

		List<Board> boards = queryFactory.selectFrom(board)
				// 검색 조건
				.where(eqBoardType(boardType))
				// 정렬 조건
				.orderBy(getOrderSpecifier(sort).stream().toArray(OrderSpecifier[]::new)).fetch();

		List<BoardDTO> result = boards.stream()
				.map(m -> BoardDTO.builder().boardNo(m.getBoardNo()).memberId(m.getMember().getMemberId())
						.title(m.getTitle()).content(m.getContent()).viewCnt(m.getViewCnt())
						.regDate(m.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.modDate(m.getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.commentCnt(commentRepository.countByBoard(m)).likesCnt(likesRespository.countByBoard(m))
						.file(m.getFile()).build())
				.collect(Collectors.toList());

		return result;
	}

	// 게시판별 검색, 조건이 없으면 null 반환
	private BooleanExpression eqBoardType(BoardType boardType) {
		return boardType != null ? board.type.eq(boardType) : null;
	}

	private Sort sortByOrderCondition(String orderCondition, String orderDirection) {

		// 기본 정렬 조건 : 등록일순, 내림차순
		Sort sort = Sort.by(Direction.DESC, "regDate");

		Direction direction = null;

		// 검색 조건이 null이 아닌 경우,
		if (orderCondition != null) {
			// 오름차순 정렬일 때, ASC direction 지정
			if (orderDirection.toUpperCase().equals(OrderDirection.ASC)) {
				direction = Direction.ASC;
			}
			// 기본 정렬 조건 : DESC
			else
				direction = Direction.DESC;

			sort = Sort.by(direction, orderCondition);
		}

		return sort;
	}

	private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
		List<OrderSpecifier> orderBy = new ArrayList<OrderSpecifier>();

		sort.stream().forEach(order -> {
			// 정렬 조건
			Order direction = order.isAscending() ? Order.ASC : Order.DESC;

			// 정렬 기준 컬럼
			String prop = order.getProperty();

			// QBoard
			PathBuilder<Board> pathBuilder = new PathBuilder<Board>(Board.class, "board");
			orderBy.add(new OrderSpecifier(direction, pathBuilder.get(prop)));

		});

		return orderBy;
	}

	@Override
	public BoardDTO findByBoardId(Long boardId) {

		Board board = boardRepository.findById(boardId).orElseThrow(() -> {
			throw new NotFoundException("해당하는 게시물을 찾을 수 없습니다.");
		});

		board.setViewCnt(board.getViewCnt() + 1);

		Long likesCnt = likesRespository.countByBoard(board);

		BoardDTO result = BoardDTO.builder().boardNo(board.getBoardNo()).memberId(board.getMember().getMemberId())
				.title(board.getTitle()).content(board.getContent()).viewCnt(board.getViewCnt()).type(board.getType())
				.regDate(board.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.modDate(board.getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).gym(board.getGym())
				.commentCnt(commentRepository.countByBoard(board)).likesCnt(likesCnt)
//				.comments(board.getComments())
				.file(board.getFile()).build();

		return result;
	}

	@Override
	public List<BoardDTO> findBoardByMemberId(String memberId) {
		Member savedMember = memberRepository.findById(memberId).get();
		List<Board> boards = boardRepository.findByMember(savedMember);

		List<BoardDTO> result = boards.stream()
				.map(m -> BoardDTO.builder().boardNo(m.getBoardNo()).memberId(m.getMember().getMemberId())
						.title(m.getTitle()).content(m.getContent()).viewCnt(m.getViewCnt())
						.regDate(m.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.modDate(m.getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.commentCnt(commentRepository.countByBoard(m)).likesCnt(likesRespository.countByBoard(m))
						.file(m.getFile()).build())
				.collect(Collectors.toList());
		
		return result;
	}

}
