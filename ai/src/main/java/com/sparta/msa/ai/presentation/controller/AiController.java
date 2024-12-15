package com.sparta.msa.ai.presentation.controller;

import com.sparta.msa.ai.domain.model.AiResponse;
import com.sparta.msa.ai.domain.service.AiService;
import com.sparta.msa.ai.infrastructure.repository.AiResponseRepository;
import com.sparta.msa.ai.presentation.request.AiMessageRequestDto;
import com.sparta.msa.ai.presentation.response.AiMessageCreateResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class AiController {

    private final AiService aiService;
    private final AiResponseRepository aiResponseRepository;

    public AiController(AiService aiService, AiResponseRepository aiResponseRepository) {
        this.aiService = aiService;
        this.aiResponseRepository = aiResponseRepository;
    }

    @PostMapping("/ai")
    public ResponseEntity<AiMessageCreateResponseDto> createAiMessage(@RequestBody AiMessageRequestDto requestDto) {
        AiMessageCreateResponseDto responseDto = aiService.createAiMessage(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 조회
    @GetMapping("/ai/{aiResponseId}")
    public AiMessageCreateResponseDto getAiResponseById(@PathVariable UUID aiResponseId) {
        AiResponse aiResponse = aiResponseRepository.findById(aiResponseId)
                .orElseThrow(() -> new RuntimeException("AI Response not found"));

        return AiMessageCreateResponseDto.builder()
                .content(aiResponse.getAnswer())
                .build();
    }
}
