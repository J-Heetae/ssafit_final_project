package com.ssafy.ssafit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.service.VideoService;
import com.ssafy.ssafit.service.VideoServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class AdminController {
	
	private final VideoServiceImpl videoService;
	
	@GetMapping("/{no}")
	public ResponseEntity<Video> findVideo(@PathVariable("no") Long no)  {
		return new ResponseEntity<Video>(videoService.findByNo(no), HttpStatus.OK);
	}

}
