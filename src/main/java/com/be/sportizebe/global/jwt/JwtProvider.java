package com.be.sportizebe.global.jwt;

import com.be.sportizebe.domain.auth.exception.AuthErrorCode;
import com.be.sportizebe.global.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtProvider {

  private SecretKey key;
  private final String secretKey;
  @Getter private final long accessTokenExpireTime;
  @Getter private final long refreshTokenExpireTime;

  public JwtProvider(
      @Value("${spring.jwt.secret}") String secretKey,
      @Value("${spring.jwt.access-token-expire-time}") long accessTokenExpireTime,
      @Value("${spring.jwt.refresh-token-expire-time}") long refreshTokenExpireTime) {
    this.secretKey = secretKey;
    this.accessTokenExpireTime = accessTokenExpireTime;
    this.refreshTokenExpireTime = refreshTokenExpireTime;
  }

  @PostConstruct
  public void init() {
    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String createAccessToken(Long userId) {
    return createToken(userId, accessTokenExpireTime);
  }

  public String createRefreshToken(Long userId) {
    return createToken(userId, refreshTokenExpireTime);
  }

  private String createToken(Long userId, long expireTimeMillis) {
    Date now = new Date();
    return Jwts.builder()
        .subject(String.valueOf(userId))
        .id(UUID.randomUUID().toString())
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expireTimeMillis))
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new CustomException(AuthErrorCode.JWT_TOKEN_EXPIRED);
    } catch (UnsupportedJwtException e) {
      throw new CustomException(AuthErrorCode.UNSUPPORTED_TOKEN);
    } catch (MalformedJwtException e) {
      throw new CustomException(AuthErrorCode.MALFORMED_JWT_TOKEN);
    } catch (SecurityException e) {
      throw new CustomException(AuthErrorCode.INVALID_SIGNATURE);
    } catch (IllegalArgumentException e) {
      throw new CustomException(AuthErrorCode.ILLEGAL_ARGUMENT);
    }
  }

  // 토큰 소유자 정보 추출
  public String extractSocialId(String token) {
    return parseClaims(token).getSubject();
  }

  // 토큰 Id 정보 추출 (RefershToken에 사용)
  public String extractTokenId(String token) {
    return parseClaims(token).getId();
  }

  private Claims parseClaims(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
