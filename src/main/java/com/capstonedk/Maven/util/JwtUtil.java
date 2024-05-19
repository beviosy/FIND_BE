package com.capstonedk.Maven.util;

import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.accessToken.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshToken.expiration}")
    private long refreshTokenExpiration;

    private final SecretKey secretKey;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtUtil(TokenBlacklistService tokenBlacklistService) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getLoginId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getLoginId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        String newAccessToken = Jwts.builder()
                .setSubject(claims.getSubject())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey)
                .compact();

        blacklistToken(refreshToken); // 블랙리스트에 추가
        return newAccessToken;
    }

    public void blacklistToken(String token) {
        tokenBlacklistService.blacklistToken(token);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                return false;
            }
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date expirationDate = claims.getExpiration();
        long expirationTime = expirationDate.getTime() - claims.getIssuedAt().getTime();
        return expirationTime == refreshTokenExpiration;
    }

    public boolean isAccessToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date expirationDate = claims.getExpiration();
        long expirationTime = expirationDate.getTime() - claims.getIssuedAt().getTime();
        return expirationTime == accessTokenExpiration;
    }
}
