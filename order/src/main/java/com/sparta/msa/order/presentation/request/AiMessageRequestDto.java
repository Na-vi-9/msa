package com.sparta.msa.order.presentation.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiMessageRequestDto {
    private String question;
}
