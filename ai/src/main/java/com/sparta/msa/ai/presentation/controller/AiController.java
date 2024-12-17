package com.sparta.msa.ai.presentation.controller;

import com.sparta.msa.ai.domain.model.AiResponse;
import com.sparta.msa.ai.domain.service.AiService;
import com.sparta.msa.ai.domain.service.UserFeignClient;
import com.sparta.msa.ai.infrastructure.repository.AiResponseRepository;
import com.sparta.msa.ai.infrastructure.utils.AuthorizationUtils;
import com.sparta.msa.ai.presentation.request.AiMessageRequestDto;
import com.sparta.msa.ai.presentation.response.AiMessageCreateResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class AiController {

    private final AiService aiService;
    private final AiResponseRepository aiResponseRepository;
    private final AuthorizationUtils authorizationUtils; // 추가
    private final UserFeignClient userFeignClient;       // 추가

    public AiController(AiService aiService,
                        AiResponseRepository aiResponseRepository,
                        AuthorizationUtils authorizationUtils,
                        UserFeignClient userFeignClient) { // 생성자 주입에 추가
        this.aiService = aiService;
        this.aiResponseRepository = aiResponseRepository;
        this.authorizationUtils = authorizationUtils; // 추가
        this.userFeignClient = userFeignClient;       // 추가
    }

    // AI 메시지 생성
    @PostMapping("/ai")
    public ResponseEntity<?> createAiMessage(@RequestBody AiMessageRequestDto requestDto,
                                             @RequestHeader("Authorization") String token) {
        System.out.println("Received Authorization Header: " + token); // Authorization Header 확인

        // Username 확인
        String username = null;
        try {
            username = authorizationUtils.getUsernameFromToken(token);
            System.out.println("Extracted username from token in Controller: " + username);
        } catch (Exception e) {
            System.err.println("Failed to extract username from token: " + e.getMessage());
        }

        // Slack User ID 확인
        String slackUserId = null;
        try {
            slackUserId = userFeignClient.getSlackIdByUsername(username, token);
            System.out.println("Retrieved Slack User ID in Controller: " + slackUserId);
        } catch (Exception e) {
            System.err.println("Failed to retrieve Slack User ID: " + e.getMessage());
        }

        // AI 서비스 호출
        return ResponseEntity.ok(aiService.createAiMessage(requestDto, token));
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
