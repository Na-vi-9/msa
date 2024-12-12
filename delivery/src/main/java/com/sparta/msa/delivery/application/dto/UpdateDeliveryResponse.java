package com.sparta.msa.delivery.application.dto;

import com.sparta.msa.delivery.domain.model.Delivery;
import com.sparta.msa.delivery.domain.model.DeliveryStatus;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateDeliveryResponse {
    private UUID uuid;
    private UUID orderUUID;
    private UUID departureHubUUID;
    private UUID arrivalHubUUID;
    private DeliveryStatus status;
    private String deliveryAddress;
    private String recipientUsername;
    private String deliveryManagerUsername;

    public static UpdateDeliveryResponse of(Delivery delivery) {
        return UpdateDeliveryResponse.builder()
                .uuid(delivery.getUuid())
                .orderUUID(delivery.getOrderUUID())
                .departureHubUUID(delivery.getDepartureHubUUID())
                .arrivalHubUUID(delivery.getArrivalHubUUID())
                .status(delivery.getStatus())
                .deliveryAddress(delivery.getDeliveryAddress())
                .recipientUsername(delivery.getRecipientUsername())
                .deliveryManagerUsername(delivery.getDeliveryManagerUsername())
                .build();
    }
}
