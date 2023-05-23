package com.ssafy.ssafit.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.ssafit.util.JWTUtil;

public class AdminInterceptor implements HandlerInterceptor {
	private static final String HEADER_AUTH = "access-token";

	@Value("${jwt.secret}")
	private String secretKey;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String token = request.getHeader(HEADER_AUTH);

		String memberId = JWTUtil.getUserId(token, secretKey);

		// 관리자 아이디는 admin으로 고정
		if (token != null && !JWTUtil.isExpired(token, secretKey) && memberId.equals("admin"))
			return true;

		throw new Exception("유효하지 않는 접근입니다.");

	}
}
