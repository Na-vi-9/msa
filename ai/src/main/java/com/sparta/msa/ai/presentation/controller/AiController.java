package com.sparta.msa.ai.presentation.controller;

import com.sparta.msa.ai.domain.service.AiService;
import com.sparta.msa.ai.presentation.request.AiMessageRequestDto;
import com.sparta.msa.ai.presentation.response.AiMessageCreateResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ai")
    public ResponseEntity<AiMessageCreateResponseDto> createAiMessage(@RequestBody AiMessageRequestDto requestDto) {
        AiMessageCreateResponseDto responseDto = aiService.createAiMessage(requestDto);
        return ResponseEntity.ok(responseDto);
    }

}
