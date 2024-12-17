package com.sparta.msa.ai.domain.service;

import com.sparta.msa.ai.domain.model.AiResponse;
import com.sparta.msa.ai.infrastructure.repository.AiResponseRepository;
import com.sparta.msa.ai.infrastructure.utils.AuthorizationUtils;
import com.sparta.msa.ai.presentation.request.AiMessageRequestDto;
import com.sparta.msa.ai.presentation.request.AiRequestDto;
import com.sparta.msa.ai.presentation.request.GeminiClientRequestDto;
import com.sparta.msa.ai.presentation.response.AiMessageCreateResponseDto;
import com.sparta.msa.ai.presentation.response.GeminiClientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AiService {

    private final GeminiClientService geminiClientService;
    private final AiResponseRepository aiResponseRepository;
    private final AlertFeignClient alertFeignClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${SLACK_CHANNEL_ID}")
    private String slackChannelId;

    public AiMessageCreateResponseDto createAiMessageAndNotifyChannel(AiMessageRequestDto requestDto, String token) {
        // Gemini API 요청
        GeminiClientRequestDto geminiRequest = GeminiClientRequestDto.create(
                requestDto.getQuestion(),
                ", 위 내용을 기반으로 몇월 며칠 오전/오후 몇시까지 최종 발송 시한을 도출해줘"
        );

        // Gemini API 호출 및 응답 처리
        GeminiClientResponseDto responseDto = geminiClientService.sendPrompt(apiKey, geminiRequest);
        String aiResponseText = responseDto.getCandidates()
                .get(0).getContent().getParts().get(0).getText();

        // AI 응답 저장
        AiResponse savedResponse = aiResponseRepository.save(AiResponse.builder().answer(aiResponseText).build());

        // Slack 알림 전송
        sendToSlackChannel(savedResponse.getId(), token);

        // 결과 반환
        return AiMessageCreateResponseDto.builder()
                .content(aiResponseText)
                .build();
    }

    private void sendToSlackChannel(UUID aiResponseId, String token) {
        try {
            // JSON 객체 생성
            AiRequestDto requestDto = new AiRequestDto(aiResponseId);

            // Slack 채널로 알림 전송
            alertFeignClient.sendAlert(requestDto, "Bearer " + token);
            System.out.println("Slack 채널 알림 전송 성공!");

        } catch (Exception e) {
            throw new RuntimeException("Slack 채널 알림 전송 실패", e);
        }
    }

}
