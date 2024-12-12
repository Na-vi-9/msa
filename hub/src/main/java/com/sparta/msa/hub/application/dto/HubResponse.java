package com.sparta.msa.hub.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa.hub.domain.entity.Hub;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class HubResponse implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID hubUUID;

    private String hubName;
    private String hubAddress;
    private Double hubLatitude;
    private Double hubLongitude;
    private Long hubManagerId;

    public static HubResponse fromHub(Hub hub) {
        return HubResponse.builder()
                .hubUUID(hub.getHubUUID())
                .hubName(hub.getName())
                .hubAddress(hub.getAddress())
                .hubLatitude(hub.getLatitude())
                .hubLongitude(hub.getLongitude())
                .hubManagerId(hub.getManagerId())
                .build();
    }
}
