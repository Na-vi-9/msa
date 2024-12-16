package com.sparta.msa.delivery.application.dto.hubRoute;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class GetHubRouteRequest {
    private UUID departureHubUUID;
    private UUID arrivalHubUUID;

    public static GetHubRouteRequest of(UUID departureHubUUID, UUID arrivalHubUUID) {
        return GetHubRouteRequest.builder()
                .departureHubUUID(departureHubUUID)
                .arrivalHubUUID(arrivalHubUUID)
                .build();
    }
}
