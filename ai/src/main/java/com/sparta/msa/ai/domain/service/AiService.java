package com.sparta.msa.ai.domain.service;

import com.sparta.msa.ai.presentation.request.AiMessageRequestDto;
import com.sparta.msa.ai.presentation.request.GeminiClientRequestDto;
import com.sparta.msa.ai.presentation.response.AiMessageCreateResponseDto;
import com.sparta.msa.ai.presentation.response.GeminiClientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AiService {
    private static final String FORMATTING_MESSAGE = ", 위 내용을 기반으로 몇월 며칠 오전/오후 몇시까지 최종 발송 시한을 도출해줘";
    private static final String FINAL_DEADLINE_FORMAT = ", 답변 형식은 위 내용을 기반으로 도출된 최종 발송 시한은 몇월 며칠 오전/오후 몇시입니다.";
    private final GeminiClientService geminiClientService;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AiMessageCreateResponseDto createAiMessage(AiMessageRequestDto requestDto) {

        GeminiClientResponseDto responseDto = geminiClientService.sendPrompt(apiKey,
                GeminiClientRequestDto.create(requestDto.getQuestion() + FORMATTING_MESSAGE, FINAL_DEADLINE_FORMAT));

        return AiMessageCreateResponseDto.builder()
                .content(responseDto.getCandidates()
                        .get(0)
                        .getContent()
                        .getParts()
                        .get(0)
                        .getText())
                .build();
    }
}

