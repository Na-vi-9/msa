package com.sparta.msa.product.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {
    private UUID productUUID;
    private String name;
    private Integer quantity;
    private UUID companyUUID;
    private UUID hubUUID;
}
