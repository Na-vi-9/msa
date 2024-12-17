package com.sparta.msa.ai.presentation.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class GeminiClientRequestDto {

    @JsonProperty("contents")
    private final List<Content> contents;

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class Content {
        @JsonProperty("parts")
        private final List<Part> parts;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class Part {
        @JsonProperty("text")
        private final String text;
    }

    public static GeminiClientRequestDto create(String prompt, String additionalMessage) {
        // Part 생성
        Part part = Part.builder()
                .text(prompt + additionalMessage)
                .build();

        // Content 생성
        Content content = Content.builder()
                .parts(Collections.singletonList(part))
                .build();

        // RequestBody 생성
        return GeminiClientRequestDto.builder()
                .contents(Collections.singletonList(content))
                .build();
    }
}
