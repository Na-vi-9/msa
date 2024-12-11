package com.sparta.msa.order.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderRequest {
    private UUID supplierCompanyUUID;
    private UUID receiverCompanyUUID;
    private UUID productUUID;
    private Integer quantity;
    private String memo;
}
