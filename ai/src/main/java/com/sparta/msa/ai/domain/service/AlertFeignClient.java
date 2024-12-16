package com.sparta.msa.ai.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "alert", url = "http://localhost:19010")
public interface AlertFeignClient {

    @PostMapping("/alert/send-slack")
    void sendAlert(@RequestParam UUID aiResponseId, @RequestParam String slackUserId);
}

