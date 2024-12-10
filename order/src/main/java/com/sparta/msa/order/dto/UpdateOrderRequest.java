package com.sparta.msa.order.dto;

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
