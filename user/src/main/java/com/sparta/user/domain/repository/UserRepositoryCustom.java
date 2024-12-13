package com.sparta.user.domain.repository;


import com.sparta.user.presentation.response.UserSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<UserSearchResponseDto> findUsersWithConditions(String condition, String keyword, Pageable pageable);
}

