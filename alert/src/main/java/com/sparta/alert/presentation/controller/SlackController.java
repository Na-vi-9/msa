package com.sparta.alert.presentation.controller;

import com.sparta.alert.domain.service.AlertService;
import com.sparta.alert.presentation.request.AiRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/alert")
public class SlackController {

    private final AlertService alertService;

    public SlackController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/send-slack")
    public ResponseEntity<?> sendSlackAlert(@RequestParam UUID aiResponseId,
                                            @RequestParam String slackUserId,
                                            @RequestHeader("Authorization") String token) {
        System.out.println("Received AI Response ID: " + aiResponseId);
        System.out.println("Received Slack User ID: " + slackUserId);
        System.out.println("Received Authorization Header: " + token);

        // Slack 알림 전송 로직
        return ResponseEntity.ok("Slack alert sent successfully");
    }
}
