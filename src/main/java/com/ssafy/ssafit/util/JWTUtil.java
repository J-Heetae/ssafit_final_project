package com.ssafy.ssafit.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	public static String createToken(String memberId, String secretKey, Long expMs) {
		Claims claim = Jwts.claims();
		claim.put("memberId", memberId);

		return Jwts.builder().setClaims(claim).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expMs))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	};

	public static boolean isExpired(String token, String secretKey) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration()
				.before(new Date());
	}

	public static String getUserId(String token, String secretKey) {
		return Jwts.parser().setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody().get("memberId", String.class);
	}

}
