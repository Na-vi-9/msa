package com.sparta.alert.domain.service;

import com.sparta.alert.domain.client.AiFeignClient;
import com.sparta.alert.infrastructure.utils.AuthorizationUtils;
import com.sparta.alert.presentation.response.AiMessageCreateResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AlertService {

    private final SlackService slackService;
    private final AiFeignClient aiFeignClient;
    private final AuthorizationUtils authorizationUtils;

    public AlertService(SlackService slackService,
                        AiFeignClient aiFeignClient,
                        AuthorizationUtils authorizationUtils) {
        this.slackService = slackService;
        this.aiFeignClient = aiFeignClient;
        this.authorizationUtils = authorizationUtils;
    }

    public void sendSlackChannelAlert(UUID aiResponseId, String slackChannelId, String token) {
        try {
            // AI 응답 가져오기
            AiMessageCreateResponseDto responseDto = aiFeignClient.getAiResponseById(aiResponseId);
            System.out.println("AI Response Content: " + responseDto.getContent());

            // Slack 채널에 메시지 전송
            slackService.sendMessageToChannel(slackChannelId, responseDto.getContent());

            System.out.println("Slack notification sent to channel: " + slackChannelId);

        } catch (Exception e) {
            System.err.println("Error in sendSlackChannelAlert: " + e.getMessage());
            throw new RuntimeException("Slack 채널 알림 전송 실패", e);
        }
    }
}
