package com.sparta.user.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeleteResponseDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private String slackId;

    private LocalDateTime deletedAt;
    private String deletedBy;
    private boolean isDeleted;

    public void markAsDeleted(LocalDateTime deletedAt,
                              String deletedBy) {
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.isDeleted = true;
    }
}
