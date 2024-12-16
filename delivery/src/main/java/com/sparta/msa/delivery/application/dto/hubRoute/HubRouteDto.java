package com.sparta.msa.delivery.application.dto.hubRoute;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class HubRouteDto {
    private UUID departureHubUUID;
    private UUID arrivalHubUUID;
    private Integer durationMin;
    private Double distanceKm;

}
