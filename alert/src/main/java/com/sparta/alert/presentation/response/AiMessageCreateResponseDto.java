package com.sparta.alert.presentation.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiMessageCreateResponseDto {
    private String content;
}
