package com.sparta.msa.ai.domain.service;

import com.sparta.msa.ai.domain.model.AiResponse;
import com.sparta.msa.ai.infrastructure.repository.AiResponseRepository;
import com.sparta.msa.ai.infrastructure.utils.AuthorizationUtils;
import com.sparta.msa.ai.presentation.request.AiMessageRequestDto;
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

    private static final String PROMPT_MESSAGE =
            ", 위 내용을 기반으로 몇월 며칠 오전/오후 몇시까지 최종 발송 시한을 도출해줘";
    private static final String RESPONSE_FORMAT =
            ", 답변 형식은 위 내용을 기반으로 도출된 최종 발송 시한은 몇월 며칠 오전/오후 몇시입니다.";

    private final GeminiClientService geminiClientService;
    private final AiResponseRepository aiResponseRepository;
    private final AlertFeignClient alertFeignClient;
    private final UserFeignClient userFeignClient;
    private final AuthorizationUtils authorizationUtils;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AiMessageCreateResponseDto createAiMessage(AiMessageRequestDto requestDto, String token) {
        // Gemini API 요청을 위한 메시지 생성
        String prompt = requestDto.getQuestion();
        String additionalMessage = ", 위 내용을 기반으로 몇월 며칠 오전/오후 몇시까지 최종 발송 시한을 도출해줘";

        // GeminiClientRequestDto 생성
        GeminiClientRequestDto geminiRequest = GeminiClientRequestDto.create(prompt, additionalMessage);

        // Gemini API 호출
        GeminiClientResponseDto responseDto = geminiClientService.sendPrompt(apiKey, geminiRequest);

        // 응답 데이터 추출
        String aiResponseText = extractResponseText(responseDto);

        // 응답 저장
        AiResponse savedResponse = saveAiResponse(aiResponseText);

        // Slack 알림 전송
        sendToSlack(savedResponse.getId(), token);

        // 응답 반환
        return AiMessageCreateResponseDto.builder()
                .content(aiResponseText)
                .build();
    }


    private String extractResponseText(GeminiClientResponseDto responseDto) {
        return responseDto.getCandidates()
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();
    }

    private AiResponse saveAiResponse(String responseText) {
        AiResponse aiResponse = AiResponse.builder()
                .answer(responseText)
                .build();
        return aiResponseRepository.save(aiResponse);
    }

    private void sendToSlack(UUID aiResponseId, String token) {
        try {
            // 토큰에서 username 추출
            String username = authorizationUtils.extractUsername(token);

            // username으로 Slack ID 조회
            String slackUserId = userFeignClient.getSlackIdByUsername(username);

            // Slack 알림 전송
            alertFeignClient.sendAlert(aiResponseId, slackUserId);
        } catch (Exception e) {
            throw new RuntimeException("Slack 알림 전송 실패", e);
        }
    }
}
