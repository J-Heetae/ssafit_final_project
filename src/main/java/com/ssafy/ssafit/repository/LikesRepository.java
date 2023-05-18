package com.ssafy.ssafit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long>, QuerydslPredicateExecutor<Likes> {

	Long countByBoard(Board board);
}
