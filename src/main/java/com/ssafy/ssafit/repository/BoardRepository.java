package com.ssafy.ssafit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.ssafit.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
