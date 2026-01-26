package com.be.sportizebe.global.jwt;

import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.repository.UserRepository;
import com.be.sportizebe.global.exception.CustomException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String token = resolveToken(request);

      // TODO: AccessToken과 RefreshToken 구분해서 검증하는 로직 필요함
      if (token != null && jwtProvider.validateToken(token)) {
        Long userId = Long.parseLong(jwtProvider.extractSocialId(token));
        User user = userRepository.findById(userId)
            .orElse(null);

        if (user != null) {
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  user, // User 엔티티를 직접 principal로 설정
                  null,
                  List.of(new SimpleGrantedAuthority(user.getRole().name())));
          SecurityContextHolder.getContext().setAuthentication(authentication);

          log.debug("SecurityContext에 '{}' 인증 정보를 저장했습니다.", userId);
        }
      }
    } catch (CustomException | JwtException | IllegalArgumentException e) {
      log.error("JWT 검증 실패 : {}", e.getMessage());
      SecurityContextHolder.clearContext();
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
      return;
    }
    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    log.debug("Authorization Header : {}", bearerToken);
    if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }
}
