package com.sparta.user.domain.model;

import com.sparta.user.presentation.request.UserInfoUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "p_user")

public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String slackId;

    @Column(nullable = false)
    private UserRoleEnum role;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private LocalDateTime deletedAt;

    private String deletedBy;

    private boolean isDeleted;

    public void updateRole(UserRoleEnum role, String jwtTokenUserName) {
        this.role = role;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = jwtTokenUserName;
    }

    public void updateUserInfo(UserInfoUpdateRequestDto userInfoUpdateRequestDto, String password, String jwtTokenUserName) {
        this.username = userInfoUpdateRequestDto.getUsername();
        this.password = password;
        this.name = userInfoUpdateRequestDto.getName();
        this.email = userInfoUpdateRequestDto.getEmail();
        this.slackId = userInfoUpdateRequestDto.getSlackId();
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = jwtTokenUserName;

    }

    public void markAsDeleted(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.isDeleted = true;
    }
}