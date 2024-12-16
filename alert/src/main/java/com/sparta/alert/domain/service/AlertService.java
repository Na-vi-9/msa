package com.sparta.alert.domain.service;

import com.sparta.alert.domain.client.AiFeignClient;
import com.sparta.alert.domain.client.UserFeignClient;
import com.sparta.alert.presentation.response.AiMessageCreateResponseDto;
import com.sparta.alert.infrastructure.utils.AuthorizationUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AlertService {

    private final SlackService slackService;
    private final AiFeignClient aiFeignClient;
    private final UserFeignClient userFeignClient;
    private final AuthorizationUtils authorizationUtils;

    public AlertService(SlackService slackService,
                        AiFeignClient aiFeignClient,
                        UserFeignClient userFeignClient,
                        AuthorizationUtils authorizationUtils) {
        this.slackService = slackService;
        this.aiFeignClient = aiFeignClient;
        this.userFeignClient = userFeignClient;
        this.authorizationUtils = authorizationUtils;
    }

    public void sendFinalDeadline(UUID aiResponseId, String token) {
        // 토큰에서 username 추출
        String username = authorizationUtils.extractUsername(token);

        // username을 기반으로 Slack ID 조회
        String slackUserId = userFeignClient.getSlackIdByUsername(username);

        // AI 응답 가져오기
        AiMessageCreateResponseDto responseDto = aiFeignClient.getAiResponseById(aiResponseId);

        // Slack에 메시지 전송
        try {
            slackService.sendMessage(slackUserId, responseDto.getContent());
        } catch (Exception e) {
            throw new RuntimeException("Error sending message to Slack", e);
        }
    }
}
