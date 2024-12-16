package com.sparta.alert.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AiRequestDto {
    private UUID aiResponseId;

}
