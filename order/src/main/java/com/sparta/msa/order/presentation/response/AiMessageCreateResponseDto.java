package com.sparta.msa.order.presentation.response;

import java.util.UUID;

public class AiMessageCreateResponseDto {
    private String content;
    private UUID aiResponseId;

    // 디폴트 생성자 추가
    public AiMessageCreateResponseDto() {
    }

    // 모든 필드 포함 생성자
    public AiMessageCreateResponseDto(String content, UUID aiResponseId) {
        this.content = content;
        this.aiResponseId = aiResponseId;
    }

    // Getter/Setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getAiResponseId() {
        return aiResponseId;
    }

    public void setAiResponseId(UUID aiResponseId) {
        this.aiResponseId = aiResponseId;
    }
}
