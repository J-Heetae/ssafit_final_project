package com.ssafy.ssafit;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ssafy.ssafit.util.FileUtil;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.ssafy.ssafit.repository") 스프링 부트 사용시 설정해줄 필요없음
public class SsafitFinalProjectApplication {

	@Autowired
	private static ServletContext context;

	public static void main(String[] args) {
		SpringApplication.run(SsafitFinalProjectApplication.class, args);

	}

}
