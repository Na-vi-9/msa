package com.sparta.msa.ai.domain.service;


import com.sparta.msa.ai.presentation.request.GeminiClientRequestDto;
import com.sparta.msa.ai.presentation.response.GeminiClientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gemini-api", url = "${gemini.api.url}")
public interface GeminiClientService {

    @PostMapping
    GeminiClientResponseDto sendPrompt(@RequestParam("key") String apiKey, @RequestBody GeminiClientRequestDto geminiClientRequestDto);
}