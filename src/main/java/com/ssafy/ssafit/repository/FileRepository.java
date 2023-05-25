package com.ssafy.ssafit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.ssafit.domain.File;

public interface FileRepository extends JpaRepository<File, Long>{

}
