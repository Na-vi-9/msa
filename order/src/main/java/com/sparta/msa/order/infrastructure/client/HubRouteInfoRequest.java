package com.sparta.msa.order.infrastructure.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubRouteInfoRequest {
    private UUID departureHubUUID;
    private UUID arrivalHubUUID;
}