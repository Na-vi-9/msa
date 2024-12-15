package com.sparta.msa.ai.domain.service;

import com.sparta.msa.ai.presentation.request.AiRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="alert", url = "http://localhost:19010")
public interface AlertFeignClient {

    @PostMapping("/alert")
    void sendAlert(@RequestBody AiRequestDto requestDto);
}