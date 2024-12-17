package com.sparta.msa.ai.domain.service;

import com.sparta.msa.ai.presentation.request.AiRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "alert", url = "http://localhost:19010")
public interface AlertFeignClient {

    @PostMapping("/alert/send-slack")
    void sendAlert(@RequestBody AiRequestDto aiRequestDto,
                   @RequestHeader("Authorization") String token);
}