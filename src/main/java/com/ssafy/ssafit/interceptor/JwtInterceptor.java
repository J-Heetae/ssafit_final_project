package com.ssafy.ssafit.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.ssafit.util.JWTUtil;

@Component
public class JwtInterceptor implements HandlerInterceptor {
	private static final String HEADER_AUTH = "access-token";

	@Value("${jwt.secret}")
	private String secretKey;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (request.getMethod().equals("OPTIONS"))
			return true;

		String token = request.getHeader(HEADER_AUTH);

		if (token != null && !JWTUtil.isExpired(token, secretKey))
			return true;

		throw new Exception("로그인 후 이용해주세요.");

	}

}
