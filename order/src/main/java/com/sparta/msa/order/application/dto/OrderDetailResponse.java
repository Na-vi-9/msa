package com.sparta.msa.order.application.dto;

import com.sparta.msa.order.domain.model.Order;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderDetailResponse {

    private final UUID supplierCompanyUUID;
    private final UUID receiverCompanyUUID;
    private final UUID productUUID;
    private final Integer quantity;
    private final String memo;
    private final UUID deliveryUUID;

    @Builder
    public OrderDetailResponse(UUID supplierCompanyUUID, UUID receiverCompanyUUID, UUID productUUID,
                               Integer quantity, String memo, UUID deliveryUUID) {
        this.supplierCompanyUUID = supplierCompanyUUID;
        this.receiverCompanyUUID = receiverCompanyUUID;
        this.productUUID = productUUID;
        this.quantity = quantity;
        this.memo = memo;
        this.deliveryUUID = deliveryUUID;
    }

    public OrderDetailResponse(Order order) {
        this.supplierCompanyUUID = order.getSupplierCompanyUUID();
        this.receiverCompanyUUID = order.getReceiverCompanyUUID();
        this.productUUID = order.getProductUUID();
        this.quantity = order.getQuantity();
        this.memo = order.getMemo();
        this.deliveryUUID = order.getDeliveryUUID();
    }
}
