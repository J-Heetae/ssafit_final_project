package com.ssafy.ssafit.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileUtil {

	private final String upload_path = "C:/images/";

	public com.ssafy.ssafit.domain.File upload(MultipartFile upload_file) {

		if(upload_file == null)
			return null;
		// 실제 파일 이름
		String originalFileName = upload_file.getOriginalFilename();
		String savedFileName = UUID.randomUUID().toString() + originalFileName;

		File folder = new File(upload_path);
		if (!folder.exists()) {
			// 폴더 만들기
			folder.mkdir();
		}

		File target = new File(upload_path, savedFileName);

		try {
			// 위 경로에 저장
			FileCopyUtils.copy(upload_file.getBytes(), target);
		} catch (IOException e) {
			System.out.println("에러 발생");
			e.printStackTrace();
		}

		return com.ssafy.ssafit.domain.File.builder().originalName(originalFileName).saveName(savedFileName).build();
	}

}
