package com.sparta.alert.domain.service;

import com.sparta.alert.presentation.response.AiMessageCreateResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ai", url = "http://localhost:19099")
public interface AiFeignClient {

    @GetMapping("/ai/{aiResponseId}")
    AiMessageCreateResponseDto getAiResponseById(@PathVariable UUID aiResponseId);
}
