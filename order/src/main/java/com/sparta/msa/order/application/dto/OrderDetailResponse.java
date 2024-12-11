package com.sparta.msa.order.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderDetailResponse {
    private final UUID supplierCompanyUUID;
    private final UUID receiverCompanyUUID;
    private final UUID productUUID;
    private final Integer quantity;
    private final String memo;
    private final UUID deliveryUUID;
}
