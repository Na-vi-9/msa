package com.sparta.msa.order.infrastructure.client;

import com.sparta.msa.order.infrastructure.configuration.FeignConfig;
import com.sparta.msa.order.presentation.request.AiMessageRequestDto;
import com.sparta.msa.order.presentation.request.GeminiClientRequestDto;
import com.sparta.msa.order.presentation.response.AiMessageCreateResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "ai", url = "http://localhost:19099", configuration = FeignConfig.class)
public interface AiFeignClient {

    @PostMapping("/create-and-alert")
    AiMessageCreateResponseDto createAiMessage(
            @RequestHeader("Authorization") String authorization,
            @RequestBody GeminiClientRequestDto request);
}