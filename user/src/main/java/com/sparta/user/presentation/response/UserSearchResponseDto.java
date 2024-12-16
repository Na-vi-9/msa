package com.sparta.user.presentation.response;

import com.sparta.user.domain.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResponseDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private UserRoleEnum role;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private String deletedBy;
    private LocalDateTime deletedAt;
    }
