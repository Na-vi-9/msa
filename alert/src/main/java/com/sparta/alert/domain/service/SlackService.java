package com.sparta.alert.domain.service;

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

    @Value("${slack.token}")
    private String slackToken;

    public void sendMessageToUser(String userSlackId, String message) {
        try {
            // Slack 메시지 전송
            ChatPostMessageResponse response = slack.methods(slackToken)
                    .chatPostMessage(ChatPostMessageRequest.builder()
                            .channel(userSlackId)
                            .text(message)
                            .build());

            if (!response.isOk()) {
                throw new RuntimeException("Error sending Slack message: " + response.getError());
            }

            System.out.println("Message sent successfully to user: " + userSlackId);

        } catch (IOException | SlackApiException e) {
            throw new RuntimeException("Failed to send Slack message to user", e);
        }
    }
}
