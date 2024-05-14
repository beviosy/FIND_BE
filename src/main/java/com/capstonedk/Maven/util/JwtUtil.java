package com.capstonedk.Maven.util;

import com.capstonedk.Maven.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    // 액세스 토큰 만료 시간 설정
    @Value("${jwt.accessToken.expiration}")
    private long accessTokenExpiration;

    // 리프레시 토큰 만료 시간 설정
    @Value("${jwt.refreshToken.expiration}")
    private long refreshTokenExpiration;

    // 시크릿 키를 설정하여 SecretKey 객체 생성
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 액세스 토큰 생성
    public String generateAccessToken(User user) {
        return generateToken(user.getLoginId(), accessTokenExpiration);
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(User user) {
        return generateToken(user.getLoginId(), refreshTokenExpiration);
    }

    // 토큰 생성
    private String generateToken(String subject, long expirationTime) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
