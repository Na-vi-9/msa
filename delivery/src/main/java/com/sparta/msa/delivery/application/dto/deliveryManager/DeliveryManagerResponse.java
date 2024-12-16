package com.sparta.msa.delivery.application.dto.deliveryManager;

import com.sparta.msa.delivery.domain.model.DeliveryManager;
import com.sparta.msa.delivery.domain.model.DeliveryManagerType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DeliveryManagerResponse {
    private String username;
    private UUID hubUUID;
    private DeliveryManagerType type;
    private int deliveryOrder;

    public DeliveryManagerResponse(DeliveryManager deliveryManager) {
        this.username = deliveryManager.getUsername();
        this.hubUUID = deliveryManager.getHubUUID();
        this.type = deliveryManager.getType();
        this.deliveryOrder = deliveryManager.getDeliveryOrder();
    }
}
