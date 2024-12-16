package com.sparta.msa.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.msa.product.domain.model.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private UUID productUUID;
    private String name;
    private Integer quantity;
    private UUID companyUUID;
    private UUID hubUUID;

    @QueryProjection
    public ProductResponse(Product product) {
        this.productUUID = product.getUuid();
        this.name = product.getName();
        this.quantity = product.getQuantity();
        this.companyUUID = product.getCompanyUUID();
        this.hubUUID = product.getHubUUID();
    }
}
