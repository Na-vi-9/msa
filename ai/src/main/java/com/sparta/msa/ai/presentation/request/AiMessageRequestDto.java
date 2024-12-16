package com.sparta.msa.ai.presentation.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiMessageRequestDto {
    private String question;
}
