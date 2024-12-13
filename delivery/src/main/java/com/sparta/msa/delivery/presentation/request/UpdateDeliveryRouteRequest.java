package com.sparta.msa.delivery.presentation.request;

import com.sparta.msa.delivery.application.dto.deliveryRoute.UpdateDeliveryRouteDto;
import com.sparta.msa.delivery.domain.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateDeliveryRouteRequest {
    @NotNull
    private UUID deliveryUUID;

    @NotNull
    private UUID departureHubUUID;

    @NotNull
    private UUID arrivalHubUUID;

    @NotNull
    private Integer sequence;

    @NotNull
    private Double estimatedDistanceKm;

    @NotNull
    private Integer estimatedTimeMin;

    @NotNull
    private Double actualDistanceKm;

    @NotNull
    private Integer actualTimeMin;

    @NotNull
    private DeliveryStatus status;

    @NotNull
    @Size(max = 10)
    private String deliveryManagerUsername;

    public UpdateDeliveryRouteDto toDto() {
        return UpdateDeliveryRouteDto.create(this.deliveryUUID, this.departureHubUUID, this.arrivalHubUUID,
                this.sequence, this.estimatedDistanceKm, this.estimatedTimeMin, this.actualDistanceKm, this.actualTimeMin,
                this.status, this.deliveryManagerUsername);
    }

}
