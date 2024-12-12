package com.sparta.msa.delivery.application.dto.deliveryRoute;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CreateDeliveryRouteDto {
    private UUID deliveryUUID;
    private UUID departureHubUUID;
    private UUID arrivalHubUUID;

    public static CreateDeliveryRouteDto create(UUID deliveryUUID, UUID departureHubUUID, UUID arrivalHubUUID) {
        return CreateDeliveryRouteDto.builder()
                .deliveryUUID(deliveryUUID)
                .departureHubUUID(departureHubUUID)
                .arrivalHubUUID(arrivalHubUUID)
                .build();
    }

}
