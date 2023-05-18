package com.ssafy.ssafit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.ssafit.domain.ErrorResult;
import com.ssafy.ssafit.exception.DuplicatedException;
import com.ssafy.ssafit.exception.NotFoundException;

@RestControllerAdvice(annotations = RestController.class)
public class ExControllerAdvice {
	
	@ExceptionHandler
	public ResponseEntity<ErrorResult> notFoundExHandle(NotFoundException e) {
		ErrorResult errorResult = new ErrorResult(e.getMessage());
		return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResult> duplicateExHandle(DuplicatedException e) {
		ErrorResult errorResult = new ErrorResult(e.getMessage());
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

}
