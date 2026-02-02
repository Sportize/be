package com.be.sportizebe.global.security;

import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
  private final UserRepository userRepository;

  @Cacheable(cacheNames = "userDetails", key = "#username", unless = "#result == null")
  // result == null: 유저가 있으면- > 캐시에 저장, 유저가 없으면 -> 캐시에 저장 X
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
    return new CustomUserDetails(user);
  }
}
