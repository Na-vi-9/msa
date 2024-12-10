package com.sparta.msa.order.application.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CreateOrderRequest {
    private UUID supplierCompanyUUID;
    private UUID receiverCompanyUUID;
    private UUID productUUID;
    private Integer quantity;
    private String memo;
}
