package com.sparta.alert.presentation.controller;

import com.sparta.alert.domain.service.AlertService;
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
    public String sendSlackMessage(@RequestParam UUID aiResponseId,
                                   @RequestHeader("Authorization") String token) {
        try {
            alertService.sendFinalDeadline(aiResponseId, token);
            return "Slack DM sent successfully!";
        } catch (Exception e) {
            return "Error occurred while sending Slack DM: " + e.getMessage();
        }
    }
}
