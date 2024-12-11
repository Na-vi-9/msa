package com.sparta.msa.delivery.application.dto;

import com.sparta.msa.delivery.domain.model.DeliveryStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UpdateDeliveryDto {
    private UUID orderUUID;
    private DeliveryStatus status;
    private UUID departureHubUUID;
    private UUID arrivalHubUUID;
    private String deliveryAddress;
    private String username;

    public static UpdateDeliveryDto create(UUID orderUUID,
                                           DeliveryStatus status,
                                           UUID departureHubUUID,
                                           UUID arrivalHubUUID,
                                           String deliveryAddress,
                                           String username) {
        return UpdateDeliveryDto.builder()
                .orderUUID(orderUUID)
                .status(status)
                .departureHubUUID(departureHubUUID)
                .arrivalHubUUID(arrivalHubUUID)
                .deliveryAddress(deliveryAddress)
                .username(username)
                .build();
    }
}
