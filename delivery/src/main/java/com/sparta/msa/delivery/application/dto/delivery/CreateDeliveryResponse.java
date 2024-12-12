package com.sparta.msa.delivery.application.dto.delivery;

import com.sparta.msa.delivery.domain.model.Delivery;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CreateDeliveryResponse {
    private UUID deliveryUUID;

    public static CreateDeliveryResponse of(Delivery delivery) {
        return CreateDeliveryResponse.builder()
                .deliveryUUID(delivery.getUuid())
                .build();
    }
}
