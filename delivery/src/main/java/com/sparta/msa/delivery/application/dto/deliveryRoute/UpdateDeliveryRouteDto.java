package com.sparta.msa.delivery.application.dto.deliveryRoute;

import com.sparta.msa.delivery.domain.model.DeliveryStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UpdateDeliveryRouteDto {
    private UUID deliveryUUID;
    private UUID departureHubUUID;
    private UUID arrivalHubUUID;
    private Integer sequence;
    private Double estimatedDistanceKm;
    private Integer estimatedTimeMin;
    private Double actualDistanceKm;
    private Integer actualTimeMin;
    private DeliveryStatus status;
    private String deliveryManagerUsername;

    public static UpdateDeliveryRouteDto of(UUID deliveryUUID, UUID departureHubUUID, UUID arrivalHubUUID,
                                                Integer sequence, Double estimatedDistanceKm,
                                                Integer estimatedTimeMin, Double actualDistanceKm, Integer actualTimeMin,
                                                DeliveryStatus status, String deliveryManagerUsername) {
        return UpdateDeliveryRouteDto.builder()
                .deliveryUUID(deliveryUUID)
                .departureHubUUID(departureHubUUID)
                .arrivalHubUUID(arrivalHubUUID)
                .sequence(sequence)
                .estimatedDistanceKm(estimatedDistanceKm)
                .estimatedTimeMin(estimatedTimeMin)
                .actualDistanceKm(actualDistanceKm)
                .actualTimeMin(actualTimeMin)
                .status(status)
                .deliveryManagerUsername(deliveryManagerUsername)
                .build();
    }
}
