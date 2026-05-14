package com.project.secure_document_sharing.security;

import com.project.secure_document_sharing.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final JwtProperties jwtProperties;

	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		Instant expiry = now.plus(Duration.ofMinutes(jwtProperties.expirationMinutes()));
		return Jwts.builder()
				.issuer(jwtProperties.issuer())
				.subject(authentication.getName())
				.issuedAt(Date.from(now))
				.expiration(Date.from(expiry))
				.signWith(signingKey())
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public String getUsername(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(signingKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.getSubject();
	}

	public long getExpirationMinutes() {
		return jwtProperties.expirationMinutes();
	}

	private SecretKey signingKey() {
		byte[] keyBytes = jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
