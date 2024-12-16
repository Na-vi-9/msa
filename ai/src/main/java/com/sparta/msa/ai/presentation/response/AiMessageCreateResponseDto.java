package com.sparta.msa.ai.presentation.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AiMessageCreateResponseDto {
    private String content;
}
