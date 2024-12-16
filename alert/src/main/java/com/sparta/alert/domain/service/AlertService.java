package com.sparta.alert.domain.service;

import com.sparta.alert.domain.client.AiFeignClient;
import com.sparta.alert.domain.client.UserFeignClient;
import com.sparta.alert.domain.service.SlackService;
import com.sparta.alert.infrastructure.utils.AuthorizationUtils;
import com.sparta.alert.presentation.response.AiMessageCreateResponseDto;
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
        try {
            String username = authorizationUtils.extractUsername(token);
            System.out.println("Extracted Username: " + username);

            if (username == null || username.trim().isEmpty()) {
                throw new RuntimeException("Username is null or empty after extracting from token.");
            }

            String slackUserId = userFeignClient.getSlackIdByUsername(username, token);
            System.out.println("Slack User ID: " + slackUserId);

            AiMessageCreateResponseDto responseDto = aiFeignClient.getAiResponseById(aiResponseId);
            slackService.sendMessage(slackUserId, responseDto.getContent());
        } catch (Exception e) {
            System.err.println("Error in sendFinalDeadline: " + e.getMessage());
            throw new RuntimeException("Error occurred during Feign Client call", e);
        }

    }
}
