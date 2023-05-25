package com.ssafy.ssafit.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.ssafit.domain.Board;
import com.ssafy.ssafit.domain.BoardType;
import com.ssafy.ssafit.domain.File;
import com.ssafy.ssafit.dto.BoardDTO;
import com.ssafy.ssafit.service.BoardService;
import com.ssafy.ssafit.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

	private final BoardService boardService;
	private final FileUtil fileUtil;

	@GetMapping("/img")
	public ResponseEntity<Resource> display(String filename) throws IOException {
		String path = "C:/save/";

		System.out.println("path : " + path);
		Path filePath = Paths.get(path + filename);
		System.out.println(filePath);

		String contentType = Files.probeContentType(filePath);
		HttpHeaders header = new HttpHeaders();
		header.setContentDisposition(
				ContentDisposition.builder("attachment").filename(filename, StandardCharsets.UTF_8).build());
		Resource resource = new InputStreamResource(Files.newInputStream(filePath));

		return new ResponseEntity<Resource>(resource, HttpStatus.OK);
	}

	@GetMapping("/download")
	public void download(HttpServletResponse response, String filename, String originalName) throws IOException {

		String path = "C:/save/" + filename;

		byte[] fileByte = FileUtils.readFileToByteArray(new java.io.File(path));

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; fileName=\"" + URLEncoder.encode(originalName, "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");

		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();

	}

	@GetMapping("/board")
	public ResponseEntity<List<BoardDTO>> findAll(@RequestParam(required = false) BoardType boardType,
			String orderCondition, @RequestParam(defaultValue = "ASC") String orderDirection) {

		// Pageable pageable = PageRequest.of((nowPage -1), PAGE_COUNT);
		List<BoardDTO> boardList = boardService.findAllBoard(boardType, orderCondition, orderDirection);

		return new ResponseEntity<List<BoardDTO>>(boardList, HttpStatus.OK);
	}

	@GetMapping("/board/{id}")
	public ResponseEntity<BoardDTO> findBoard(@PathVariable("id") Long boardNo) {
		BoardDTO board = boardService.findByBoardId(boardNo);
		return new ResponseEntity<BoardDTO>(board, HttpStatus.OK);
	}

	@PutMapping("/board")
	public ResponseEntity<BoardDTO> updateBoard(BoardDTO board) {

		BoardDTO result = boardService.update(board);

		return new ResponseEntity<BoardDTO>(result, HttpStatus.ACCEPTED);
	}

	@PostMapping("/board")
	public ResponseEntity<BoardDTO> insertBoard(BoardDTO board, MultipartFile upload_file) {

		File savedFile = fileUtil.upload(upload_file);

		BoardDTO result = boardService.insert(board, savedFile);

		return new ResponseEntity<BoardDTO>(result, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/board/{boardNo}")
	public ResponseEntity<Void> deleteBoard(@PathVariable Long boardNo) {
		boardService.delete(boardNo);

		return new ResponseEntity<Void>(HttpStatus.OK);

	}

}
