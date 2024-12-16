package com.sparta.msa.hub.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString
@Builder(access = AccessLevel.PRIVATE)
public class CreateHubResponse implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID hubUUID;

    public static CreateHubResponse toResponse(UUID hubUUID) {
        return CreateHubResponse.builder()
                .hubUUID(hubUUID)
                .build();
    }
}
