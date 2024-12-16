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

    @Value("${slack.token}")
    private String slackToken;

    private final String slackId = "U084RTJMRLP";

    public void sendMessage(String slackUserId, String message) throws IOException, SlackApiException {
        Slack slack = Slack.getInstance();
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(slackId)
                .text(message)
                .build();

        ChatPostMessageResponse response = slack.methods(slackToken).chatPostMessage(request);

        if (!response.isOk()) {
            throw new RuntimeException("Error sending Slack DM: " + response.getError());
        }
    }
}
