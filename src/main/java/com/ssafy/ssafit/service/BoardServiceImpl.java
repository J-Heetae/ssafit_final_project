package com.ssafy.ssafit.service;

import java.util.ArrayList;
import java.util.List;

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
import com.ssafy.ssafit.domain.Member;
import com.ssafy.ssafit.domain.QBoard;
import com.ssafy.ssafit.domain.asset.OrderDirection;
import com.ssafy.ssafit.exception.NotFoundException;
import com.ssafy.ssafit.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final JPAQueryFactory queryFactory;

	private QBoard board = QBoard.board;

	@Override
	public Board insert(Board board, Member member) {
		board.setMember(member);
		Board savedBoard = boardRepository.save(board);

		return savedBoard;
	}

	@Override
	public Board update(Board board) {
		Board savedBoard = boardRepository.findById(board.getBoardNo()).orElse(null);

		// 게시글 번호에 해당하는 게시글이 없으면 예외 발생
		if (savedBoard == null)
			throw new NotFoundException("해당하는 게시물을 찾을 수 없습니다.");

		// 제목, 내용 수정
		savedBoard.setTitle(board.getTitle());
		savedBoard.setContent(board.getContent());

		return savedBoard;
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
	public Board findByBoardId(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(() -> {
			throw new NotFoundException("해당하는 게시물을 찾을 수 없습니다.");
		});
	}

}
