package com.sparta.msa.ai.domain.service;

import com.sparta.msa.ai.domain.model.AiResponse;
import com.sparta.msa.ai.infrastructure.repository.AiResponseRepository;
import com.sparta.msa.ai.presentation.request.AiMessageRequestDto;
import com.sparta.msa.ai.presentation.request.AiRequestDto;
import com.sparta.msa.ai.presentation.request.GeminiClientRequestDto;
import com.sparta.msa.ai.presentation.response.AiMessageCreateResponseDto;
import com.sparta.msa.ai.presentation.response.GeminiClientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AiService {
    private static final String FORMATTING_MESSAGE = ", 위 내용을 기반으로 몇월 며칠 오전/오후 몇시까지 최종 발송 시한을 도출해줘";
    private static final String FINAL_DEADLINE_FORMAT = ", 답변 형식은 위 내용을 기반으로 도출된 최종 발송 시한은 몇월 며칠 오전/오후 몇시입니다.";
    private final GeminiClientService geminiClientService;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Autowired
    private AiResponseRepository aiResponseRepository;
    private final AlertFeignClient alertFeignClient;


    public AiMessageCreateResponseDto createAiMessage(AiMessageRequestDto requestDto) {

        GeminiClientResponseDto responseDto = geminiClientService.sendPrompt(apiKey,
                GeminiClientRequestDto.create(requestDto.getQuestion() + FORMATTING_MESSAGE, FINAL_DEADLINE_FORMAT));

        AiResponse aiResponse = AiResponse.builder()
                .answer(responseDto.getCandidates()
                        .get(0)
                        .getContent()
                        .getParts()
                        .get(0)
                        .getText())
                .build();

        AiResponse savedResponse = aiResponseRepository.save(aiResponse);

        sendToSlack(savedResponse.getId());

        return AiMessageCreateResponseDto.builder()
                .content(responseDto.getCandidates()
                        .get(0)
                        .getContent()
                        .getParts()
                        .get(0)
                        .getText())
                .build();
    }

    public void sendToSlack(UUID aiResponseId) {
        AiRequestDto requestDto = new AiRequestDto(aiResponseId);
        alertFeignClient.sendAlert(requestDto);
    }
}

