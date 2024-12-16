package com.sparta.alert.presentation.controller;

import com.slack.api.methods.SlackApiException;
import com.sparta.alert.domain.service.AiFeignClient;
import com.sparta.alert.domain.service.SlackService;
import com.sparta.alert.presentation.request.AiRequestDto;
import com.sparta.alert.presentation.response.AiMessageCreateResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SlackController {


    private final SlackService slackService;
    private final AiFeignClient aiFeignClient;

    public SlackController(SlackService slackService, AiFeignClient aiFeignClient) {
        this.slackService = slackService;
        this.aiFeignClient = aiFeignClient;
    }

    @PostMapping("/alert")
    public String sendMessage(@RequestBody AiRequestDto aiRequestDto) throws SlackApiException, IOException {

        AiMessageCreateResponseDto responseDto = aiFeignClient.getAiResponseById(aiRequestDto.getAiResponseId());

        // slack에 전송
        slackService.sendMessage(responseDto.getContent());

        return "Message sent successfully!";
    }
}