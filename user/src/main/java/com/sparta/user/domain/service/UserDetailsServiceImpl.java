package com.sparta.user.domain.service;

import com.sparta.user.domain.model.User;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.infrastructure.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        return new UserDetailsImpl(user);
    }
}
