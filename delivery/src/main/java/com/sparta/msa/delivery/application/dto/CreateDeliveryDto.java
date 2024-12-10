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
public class CreateDeliveryDto {
    private UUID orderUUID;
    private DeliveryStatus status;
    private UUID departureHubUUID;
    private UUID arrivalHubUUID;
    private String deliveryAddress;
    private String receiverName;
    private String receiverSlackId;

    public static CreateDeliveryDto create(UUID orderUUID,
                                           DeliveryStatus status,
                                           UUID departureHubUUID,
                                           UUID arrivalHubUUID,
                                           String deliveryAddress,
                                           String receiverName,
                                           String receiverSlackId) {
        return CreateDeliveryDto.builder()
                .orderUUID(orderUUID)
                .status(status)
                .departureHubUUID(departureHubUUID)
                .arrivalHubUUID(arrivalHubUUID)
                .deliveryAddress(deliveryAddress)
                .receiverName(receiverName)
                .receiverSlackId(receiverSlackId)
                .build();
    }
}
