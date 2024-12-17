package com.sparta.alert.presentation.request;

import java.util.UUID;

public class AiRequestDto {
    private UUID aiResponseId;

    // Getter와 Setter 추가
    public UUID getAiResponseId() {
        return aiResponseId;
    }

    public void setAiResponseId(UUID aiResponseId) {
        this.aiResponseId = aiResponseId;
    }
}
