package com.sparta.msa.order.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.msa.order.domain.model.Order;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderListResponse {

    private final UUID uuid;
    private final UUID supplierCompanyUUID;
    private final UUID receiverCompanyUUID;
    private final UUID productUUID;
    private final Integer quantity;
    private final String memo;

    @QueryProjection
    public OrderListResponse(UUID uuid, UUID supplierCompanyUUID, UUID receiverCompanyUUID,
                             UUID productUUID, Integer quantity, String memo) {
        this.uuid = uuid;
        this.supplierCompanyUUID = supplierCompanyUUID;
        this.receiverCompanyUUID = receiverCompanyUUID;
        this.productUUID = productUUID;
        this.quantity = quantity;
        this.memo = memo;
    }

    public OrderListResponse(Order order) {
        this.uuid = order.getUuid();
        this.supplierCompanyUUID = order.getSupplierCompanyUUID();
        this.receiverCompanyUUID = order.getReceiverCompanyUUID();
        this.productUUID = order.getProductUUID();
        this.quantity = order.getQuantity();
        this.memo = order.getMemo();
    }

}
