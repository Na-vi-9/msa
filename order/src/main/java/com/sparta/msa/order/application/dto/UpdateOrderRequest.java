package com.sparta.msa.order.application.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UpdateOrderRequest {
    private UUID supplierCompanyUUID;
    private UUID receiverCompanyUUID;
    private UUID productUUID;
    private Integer quantity;
    private String memo;
}
