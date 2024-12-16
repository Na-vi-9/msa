package com.sparta.alert.presentation.controller;

import com.sparta.alert.domain.service.AlertService;
import com.sparta.alert.presentation.request.AiRequestDto;
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
    public String sendSlackMessage(@RequestBody AiRequestDto requestDto,
                                   @RequestHeader("Authorization") String token) {
        try {
            System.out.println("Received Request Body: " + requestDto);
            System.out.println("Received Authorization Header: " + token);

            alertService.sendFinalDeadline(requestDto.getAiResponseId(), token);
            return "Slack DM sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while sending Slack DM: " + e.getMessage();
        }
    }
}
