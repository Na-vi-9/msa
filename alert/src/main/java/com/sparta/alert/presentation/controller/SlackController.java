package com.sparta.alert.presentation.controller;

import com.sparta.alert.domain.client.UserFeignClient;
import com.sparta.alert.domain.service.AlertService;
import com.sparta.alert.domain.service.SlackService;
import com.sparta.alert.infrastructure.utils.AuthorizationUtils;
import com.sparta.alert.presentation.request.AiRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alert")
public class SlackController {

    private final AlertService alertService;
    private final UserFeignClient userFeignClient;
    private final AuthorizationUtils authorizationUtils;

    @Value("${slack.chanel-id}")
    private String slackChannelId;

    public SlackController(AlertService alertService, UserFeignClient userFeignClient, AuthorizationUtils authorizationUtils) {
        this.alertService = alertService;
        this.userFeignClient = userFeignClient;
        this.authorizationUtils = authorizationUtils;
    }

    @PostMapping("/send-slack")
    public ResponseEntity<?> sendSlackAlert(@RequestBody AiRequestDto requestDto,
                                            @RequestHeader("Authorization") String token) {
        System.out.println("Received AI Response ID: " + requestDto.getAiResponseId());
        System.out.println("Using Slack Channel ID: " + slackChannelId);

        // 1. 토큰에서 username 추출
        String username = authorizationUtils.getUsernameFromToken(token);

        // 2. user에서 slackId 가져오기
        String userSlackId = userFeignClient.getSlackIdByUsername(username, token);

        alertService.sendSlackChannelAlert(requestDto.getAiResponseId(), userSlackId, token);

        return ResponseEntity.ok("Slack alert sent successfully to channel: " + slackChannelId);
    }
}
