package com.sparta.msa.order.application.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SlackService {

    private final Slack slack = Slack.getInstance();

    @Value("${slack.bot.token}")
    private String slackToken;

    @Value("${slack.channel.id}")
    private String channelId;

    public void sendMessage(String message) {
        try {
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channelId) // 고정된 Slack 채널 ID 사용
                    .text(message)
                    .build();

            ChatPostMessageResponse response = slack.methods(slackToken).chatPostMessage(request);

            if (!response.isOk()) {
                throw new RuntimeException("Slack 메시지 전송 실패: " + response.getError());
            }
            System.out.println("Slack 메시지 전송 성공!");

        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Slack 메시지 전송 중 오류 발생", e);
        }
    }
}
