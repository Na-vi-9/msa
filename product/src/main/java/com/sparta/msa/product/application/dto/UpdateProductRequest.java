package com.sparta.msa.product.application.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateProductRequest {
    private UUID productUUID;
    private String name;
    private Integer quantity;
    private UUID companyUUID;
    private UUID hubUUID;
}
