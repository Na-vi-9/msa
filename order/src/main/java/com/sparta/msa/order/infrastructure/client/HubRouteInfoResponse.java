package com.sparta.msa.order.infrastructure.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HubRouteInfoResponse {
    private UUID departureUUID;
    private UUID arrivalUUID;
    private Integer durationMin;
    private Double distanceKm;
}
