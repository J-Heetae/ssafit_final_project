package com.ssafy.ssafit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.ssafy.ssafit.repository") 스프링 부트 사용시 설정해줄 필요없음
public class SsafitFinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsafitFinalProjectApplication.class, args);

	}

}
