package com.sparta.alert.presentation.controller;

import com.sparta.alert.domain.service.AlertService;
import com.sparta.alert.presentation.request.AiRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alert")
public class SlackController {

    private final AlertService alertService;

    @Value("${SLACK_CHANNEL_ID}")
    private String slackChannelId;

    public SlackController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/send-slack")
    public ResponseEntity<?> sendSlackAlert(@RequestBody AiRequestDto requestDto,
                                            @RequestHeader("Authorization") String token) {
        System.out.println("Received AI Response ID: " + requestDto.getAiResponseId());
        System.out.println("Using Slack Channel ID: " + slackChannelId);

        alertService.sendSlackChannelAlert(requestDto.getAiResponseId(), slackChannelId, token);

        return ResponseEntity.ok("Slack alert sent successfully to channel: " + slackChannelId);
    }
}
