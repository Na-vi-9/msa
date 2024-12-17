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
public class HubInfoResponse {
    private UUID hubUUID;
    private String hubName;
    private String hubAddress;
    private Double hubLatitude;
    private Double hubLongitude;
    private String hubManagerName;
}
