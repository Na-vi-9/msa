package com.sparta.msa.order.application.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class OrderRequest {
    private UUID supplierCompanyID;
    private UUID receiverCompanyID;
    private UUID productID;
    private Integer quantity;
    private String memo;
}
