package com.sparta.msa.delivery.application.dto.deliveryManager;

import com.sparta.msa.delivery.domain.model.DeliveryManagerType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DeliveryManagerRequest {
    private UUID hubUUID;
    private DeliveryManagerType type;
    private String username;
}
