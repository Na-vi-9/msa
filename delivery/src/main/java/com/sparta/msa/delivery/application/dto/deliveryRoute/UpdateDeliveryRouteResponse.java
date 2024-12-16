package com.sparta.msa.delivery.application.dto.deliveryRoute;

import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import com.sparta.msa.delivery.domain.model.DeliveryStatus;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class UpdateDeliveryRouteResponse {
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

    public static UpdateDeliveryRouteResponse of(DeliveryRoute deliveryRoute) {
        return UpdateDeliveryRouteResponse.builder()
                .deliveryUUID(deliveryRoute.getUuid())
                .departureHubUUID(deliveryRoute.getDepartureHubUUID())
                .arrivalHubUUID(deliveryRoute.getArrivalHubUUID())
                .sequence(deliveryRoute.getSequence())
                .estimatedDistanceKm(deliveryRoute.getEstimatedDistanceKm())
                .estimatedTimeMin(deliveryRoute.getEstimatedTimeMin())
                .actualDistanceKm(deliveryRoute.getActualDistanceKm())
                .actualTimeMin(deliveryRoute.getActualTimeMin())
                .status(deliveryRoute.getStatus())
                .deliveryManagerUsername(deliveryRoute.getDeliveryManagerUsername())
                .build();
    }
}
